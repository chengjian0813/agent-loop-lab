# 08 · 多 Agent 组队 loop：herdr-team-loop-cli

**目的**：前七篇的做法（外部状态、独立验收、预算刹车）合在一起，就是一个小型的多 Agent 团队。这里用作者开源的 [herdr-team-loop-cli](https://github.com/chengjian0813/herdr-team-loop-cli) 在本仓库上跑一轮，分四个角色：coordinator、developer、reviewer、tester。

**步骤**（具体命令以 herdr-team-loop-cli 的 README 为准）

1. 安装 herdr-team-loop-cli 及其依赖的 Herdr 运行时。
2. 在本仓库根目录初始化 workspace，任务选 02 的 fix_plan 或 04 的订单生命周期。
3. 预算按默认值（如 `max_rounds=3`）或调小，验收命令填 `./mvnw -B verify && scripts/check-test-integrity.sh origin/main`。
4. 跑完后对比单 Agent loop：轮数、reviewer 和 tester 拦下的问题、人工介入次数。
