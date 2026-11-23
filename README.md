# agent-loop-lab

[![CI](https://github.com/chengjian0813/agent-loop-lab/actions/workflows/ci.yml/badge.svg)](https://github.com/chengjian0813/agent-loop-lab/actions/workflows/ci.yml)

公众号「硅基葱蘸酱」**【Agent Loop】系列**的公共实验仓库。系列共 8 篇，每篇的实验都在这个小服务上做，读者可以自己 clone 下来复现。

## 这是什么

一个故意做得很小的 **优惠券结算服务**（Java 17 + Spring Boot 3 + Maven）。领域是虚构的，数据放内存，不需要数据库：

- `GET  /api/coupons`：列出可用优惠券（满减券、折扣券）
- `POST /api/checkout/quote`：给购物车算价，最多用一张券

```bash
curl -s localhost:8080/api/checkout/quote -H 'Content-Type: application/json' \
  -d '{"items":[{"sku":"BOOK-1","unitPrice":60,"quantity":2}],"couponCode":"SAVE20"}'
# {"subtotal":120.00,"discount":20.00,"payable":100.00,"appliedCoupon":"SAVE20"}
```

服务本身不是重点。重点是给 Agent loop 一个**能被机器判定「做没做完」**的环境：

| 护栏 | 作用 |
|---|---|
| `./mvnw verify` | 单测 + MockMvc 接口测试 + JaCoCo 行覆盖率门槛（≥ 80%） |
| `scripts/check-test-integrity.sh` | 抓「删测试、`@Disabled`、改 pom 跳过测试或降覆盖率」这类作弊 |
| `AGENTS.md` | 给 Agent 的构建命令和硬规则 |
| GitHub Actions | 每次 push / PR 跑 verify，PR 额外跑测试守卫 |

## 快速开始

需要 JDK 17。Maven 不用装，仓库自带 wrapper。

```bash
git clone https://github.com/chengjian0813/agent-loop-lab.git
cd agent-loop-lab
./mvnw -B verify                                  # 构建 + 测试 + 覆盖率门槛
open target/site/jacoco/index.html                # 覆盖率报告（Linux 用 xdg-open）
scripts/check-test-integrity.sh origin/main       # 测试完整性守卫
./mvnw spring-boot:run                            # 启动服务，端口 8080
```

## 实验目录

每篇文章对应 `docs/experiments/` 下的一个目录，里面有说明和种子文件（Prompt、计划、脚本）：

| # | 主题 | 目录 |
|---|---|---|
| 1 | Loop 到底是什么：手动喂 vs 放手转 | [01-what-is-a-loop](docs/experiments/01-what-is-a-loop/) |
| 2 | Ralph：while true 跑完一个功能 | [02-ralph](docs/experiments/02-ralph/) |
| 3 | 停止条件：测试被注释掉怎么办 | [03-stop-conditions](docs/experiments/03-stop-conditions/) |
| 4 | 长任务：progress 文件交接班 | [04-long-task-handoff](docs/experiments/04-long-task-handoff/) |
| 5 | 独立验收者 | [05-separate-validator](docs/experiments/05-separate-validator/) |
| 6 | 定时 / CI 触发，早上收 PR | [06-scheduled-trigger](docs/experiments/06-scheduled-trigger/) |
| 7 | 预算与迭代上限 | [07-budget-caps](docs/experiments/07-budget-caps/) |
| 8 | 多 Agent 组队 loop | [08-team-loop](docs/experiments/08-team-loop/) |

### 怎么复现

1. 从 `main` 切一个实验分支：`git switch -c exp/02-ralph`。
2. 按对应目录 README 的步骤，把种子文件拷到根目录，交给你的 Agent（Claude Code、Codex、Droid 等都行）。
3. 完成的判定只有一个：`./mvnw -B verify` 和 `scripts/check-test-integrity.sh` 都通过。
4. 记录轮数、耗时、花费、人工介入次数。欢迎把结果发到 Issue。

`main` 永远保持绿色，实验分支不合并回来。

## 项目结构

```
src/main/java/dev/agentloop/lab/
  coupon/     Coupon（满减 / 折扣）、Money、CouponRepository
  checkout/   CheckoutService、CartItem、Quote
  web/        REST 接口与异常处理
src/test/java/...   单测 + MockMvc 测试
scripts/check-test-integrity.sh
docs/experiments/   8 篇文章的实验
```

---

## English

**agent-loop-lab** is the shared playground for the *Agent Loop* article series (Chinese, WeChat account 硅基葱蘸酱). It is a deliberately tiny coupon-checkout service (Java 17, Spring Boot 3, Maven wrapper, JUnit 5, JaCoCo) with a fictional domain and in-memory data.

The service is not the point. The point is to give agent loops a **machine-checkable definition of done**:

- `./mvnw -B verify`: tests plus a JaCoCo line-coverage gate (≥ 80%).
- `scripts/check-test-integrity.sh [BASE_REF]`: fails if tests were deleted, the `@Test` count dropped, skip markers (`@Disabled`, `assumeTrue(false)`, …) were added, or `pom.xml` test/coverage settings were changed.
- `AGENTS.md`: commands and hard rules for coding agents.

To reproduce an experiment, branch off `main`, follow `docs/experiments/<NN>-*/README.md`, and let your agent loop run until both checks pass. Experiments cover Ralph loops, stop conditions and reward hacking, progress-file handoff, separate validators, scheduled GitHub Actions runs, budget caps, and a multi-agent team loop using [herdr-team-loop-cli](https://github.com/chengjian0813/herdr-team-loop-cli).

License: MIT.
