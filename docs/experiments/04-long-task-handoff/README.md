# 04 · 长任务交接班：靠 progress 文件，不靠大窗口

**目的**：一个需要跑很多轮的任务，对比「一个会话硬扛到底」和「每轮新会话 + progress.md 交接」，看后期是否出现遗忘、重复劳动、改坏前面的东西。

**任务**：在报价之上加订单生命周期：下单（锁定报价）→ 支付 → 取消（退回优惠券）→ 退款。内存存储即可，要求状态机非法跳转返回 409。大约 8–12 个小步骤。

**步骤**

```bash
git switch -c exp/04-handoff
cp docs/experiments/04-long-task-handoff/progress.md .
# A 组：一个会话里把 PROMPT.md 交给 Agent，一直聊到完成
# B 组：每轮新会话，都喂同一份 PROMPT.md（可复用 02 的 ralph.sh）
```

**观察点**：总轮数、后期回归（之前通过的测试被改挂）次数、progress.md 是否真实反映了进度、人工介入次数。
