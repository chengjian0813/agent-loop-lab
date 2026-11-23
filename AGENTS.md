# AGENTS.md

Instructions for coding agents (Claude Code, Codex, Droid, Cursor, etc.) working in this repo.

## Project
Tiny Spring Boot 3 coupon-checkout service (Java 17, Maven wrapper). Fictional domain, in-memory data, no DB.

- `src/main/java/dev/agentloop/lab/coupon`   – coupon types (满减 / 折扣) and an in-memory repository
- `src/main/java/dev/agentloop/lab/checkout` – `CheckoutService` prices a cart and applies one coupon
- `src/main/java/dev/agentloop/lab/web`      – REST API: `GET /api/coupons`, `POST /api/checkout/quote`

## Commands
| What | Command |
|---|---|
| Full check (the definition of done) | `./mvnw -B verify` |
| Tests only | `./mvnw -B test` |
| One test class | `./mvnw -B test -Dtest=CheckoutServiceTest` |
| Coverage report | `./mvnw -B verify` then open `target/site/jacoco/index.html` |
| Test-integrity guard | `scripts/check-test-integrity.sh origin/main` |
| Run the app | `./mvnw spring-boot:run` (port 8080) |

`./mvnw verify` runs unit + MockMvc tests and the JaCoCo gate (line coverage >= 80%).

## Rules (hard)
1. Done means `./mvnw -B verify` exits 0 **and** `scripts/check-test-integrity.sh` exits 0. Saying "done" is not done.
2. Never delete, skip or weaken tests: no `@Disabled`, no `assumeTrue(false)`, no removing assertions.
3. Never touch `scripts/check-test-integrity.sh`, `.github/workflows/`, or the coverage settings in `pom.xml`.
4. Money: `BigDecimal`, scale 2, `RoundingMode.HALF_UP` (use `Money.normalize`). Never `double`.
5. Work on one item at a time; keep commits small; update the progress file if the experiment uses one.
6. If you are stuck after 3 attempts on the same failure, stop and write what you tried to the progress file.
