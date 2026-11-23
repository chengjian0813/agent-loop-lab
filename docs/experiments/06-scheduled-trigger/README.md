# 06 · 睡前开一个 loop，早上收 PR：定时和事件触发

**目的**：把 loop 从本机终端搬到 GitHub Actions：定时（cron）或手动触发，Agent 在 runner 上干活，结果以 PR 的形式交给人审。

**种子**：`nightly-agent.yml.example`。它**默认不启用**，因为需要你自己的 Agent CLI 和密钥。

**启用步骤（在你自己的 fork 里）**

1. 仓库 Settings → Secrets 添加你的 Agent 服务 API key（示例里叫 `AGENT_API_KEY`）。
2. Settings → Actions → General：允许 Actions 创建 PR。
3. `cp docs/experiments/06-scheduled-trigger/nightly-agent.yml.example .github/workflows/nightly-agent.yml`，按注释改 Agent 命令。
4. 先用 `workflow_dispatch` 手动跑一次，确认会开 PR，再打开 cron。

**护栏**：`timeout-minutes` 硬超时；只开 PR、不直接推 main；PR 照样跑 CI 和测试守卫；合并永远由人做。

**观察点**：一晚上开了几个 PR、能直接合的比例、Actions 分钟数和模型花费。
