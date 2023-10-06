package cn.itcast.account.service.impl;

import cn.itcast.account.entity.AccountFreeze;
import cn.itcast.account.mapper.AccountFreezeMapper;
import cn.itcast.account.mapper.AccountMapper;
import cn.itcast.account.service.AccountTccService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : seata-demo
 * @Package : cn.itcast.account.service.impl
 * @ClassName : .java
 * @createTime : 2023/10/6 14:12
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class AccountTccServiceImpl implements AccountTccService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountFreezeMapper accountFreezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        // 获取当前的全局事务id
        String xid = RootContext.getXID();
        // 检查分支事务是否已经执行过 Cancel，如果是，就是业务悬挂，不进行任何处理
        AccountFreeze accountFreeze = accountFreezeMapper.selectById(xid);
        if (accountFreeze != null && accountFreeze.getState() == AccountFreeze.State.CANCEL) {
            return;
        }
        // 执行 try 逻辑
        // 尝试扣减剩余金额，如果扣减失败，数据库会报错
        accountMapper.deduct(userId, money);
        // 添加冻结金额和 Try 执行记录
        AccountFreeze af = new AccountFreeze();
        af.setState(AccountFreeze.State.TRY);
        af.setFreezeMoney(money);
        af.setUserId(userId);
        af.setXid(xid);
        accountFreezeMapper.insert(af);
    }

    @Override
    public boolean confirm(BusinessActionContext ctx) {
        String xid = ctx.getXid();
        // 确保幂等性，如果金额冻结记录已经被删除，直接返回成功
        AccountFreeze af = accountFreezeMapper.selectById(xid);
        if (af == null) {
            return true;
        }
        // 删除冻结金额和 Try 执行记录
        int rows = accountFreezeMapper.deleteById(xid);
        return rows == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext ctx) {
        // 查询冻结数据
        String xid = ctx.getXid();
        AccountFreeze af = accountFreezeMapper.selectById(xid);
        if (af == null) {
            // 没有分支事务执行记录时触发 Cancel，是空回滚
            // 只记录 Cancel 执行，不做业务处理
            AccountFreeze newAf = new AccountFreeze();
            newAf.setXid(xid);
            newAf.setUserId(ctx.getActionContext("userId").toString());
            newAf.setFreezeMoney(0);
            newAf.setState(AccountFreeze.State.CANCEL);
            accountFreezeMapper.insert(newAf);
            return true;
        }
        // 确保幂等性，如果已经执行过 Cancel，不再执行相关逻辑，直接返回成功
        if (AccountFreeze.State.CANCEL == af.getState()) {
            return true;
        }
        // 冻结金额归零，并且分支事务状态修改为 cancel
        AccountFreeze newAf = new AccountFreeze();
        newAf.setXid(xid);
        newAf.setState(AccountFreeze.State.CANCEL);
        newAf.setFreezeMoney(0);
        accountFreezeMapper.updateById(newAf);
        // 恢复可用金额
        int rows = accountMapper.refund(af.getUserId(), af.getFreezeMoney());
        return rows == 1;
    }
}
