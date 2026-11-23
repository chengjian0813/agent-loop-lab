先读 AGENTS.md 和 progress.md。你是接班的工程师，上一班的情况都在 progress.md 里。

目标：实现订单生命周期（CREATED → PAID → REFUNDED，CREATED → CANCELLED），
API：POST /api/orders（用报价请求下单）、POST /api/orders/{id}/pay、/cancel、/refund、GET /api/orders/{id}。
非法状态跳转返回 409；取消或退款后优惠券可再次使用。

本轮流程：
1. 从 progress.md 的「下一步」里挑一项做完，verify 必须通过。
2. 更新 progress.md：完成了什么、遇到的坑、下一步（具体到类名或测试名）。
3. commit。所有项完成且 verify 通过时，在 progress.md 写 `STATUS: DONE`。
