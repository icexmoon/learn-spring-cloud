package cn.itcast.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : seata-demo
 * @Package : cn.itcast.account.service
 * @ClassName : .java
 * @createTime : 2023/10/6 14:05
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description : 使用 TCC 模式分布式事务的账户操作相关接口
 */
@LocalTCC
public interface AccountTccService {
    /**
     * 扣减账户金额
     *
     * @param userId 账户id
     * @param money  金额
     */
    @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,
                @BusinessActionContextParameter(paramName = "money") int money);

    /**
     * 分布式事务成功提交时执行的操作
     * @param ctx 分支事务的上下文
     * @return 分支事务提交是否成功
     */
    boolean confirm(BusinessActionContext ctx);

    /**
     * 分布式事务失败回滚时执行的操作
     * @param ctx 分支事务的上下文
     * @return 分支事务回滚是否成功
     */
    boolean cancel(BusinessActionContext ctx);
}
