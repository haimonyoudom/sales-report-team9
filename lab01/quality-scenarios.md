 # Quality-attribute scenarios: Angkor Mart monthly sales report

## Stakeholders
- Head-office analyst: views and exports all reports; needs correct numbers by the morning of day 2.
- Branch manager (PNH, REP, BTB): views the report of their own branch only.
- Finance director: receives an e-mail when a report is ready; needs it on time.
- Operations person: runs and monitors the nightly job; needs clear errors and re-runs.
- Development team: adds formats and branches; needs fast, reliable tests.

## Scenarios
| ID | Attribute | Source | Stimulus | Artifact | Environment | Response | Response measure | Rank |
|----|-----------|--------|----------|----------|-------------|----------|------------------|------|
| QA-1 | Performance | Scheduler, 06:00 on day 2 of M+1 | Starts the report for a month of about 3 M rows | Report engine | Normal operation, 8-core server, all files complete | Report files written, "ready" event published | <= 60 s wall-clock, median of 5 runs after 2 warm-up runs | (H,H) |
| QA-2 | Scalability | Head-office management | Opens a 4th branch (about 1 M extra rows per month) | Ingest job and report engine | Normal operation, same server | Report is still produced with only a configuration change | Run time grows by <= 35 % versus the 3-branch run (median of 5 runs) and 0 lines of code changed | (M,M) |
| QA-3 | Availability | Branch REP internet link | REP's file for the last day of the month arrives 2 h late | Ingest job | Degraded: one link down, other branches complete | PNH and BTB are processed; a partial report marked "REP incomplete" is published; the late file is merged and the report regenerated | Partial report out by 07:00 on day 2 in 5 of 5 simulated runs; full report regenerated <= 10 min after the late file arrives; 0 duplicated rows | (H,M) |
| QA-4 | Modifiability | Developer | Client asks for a new export format (for example PDF) | Renderers (render package) | Design time, system in production | New renderer added with no change to model, ingest or engine | <= 2 person-days; <= 1 new class and <= 5 changed lines in existing files (measured with git diff --stat) | (H,L) |
| QA-5 | Security | Branch manager of PNH (authenticated) | Requests the report of REP by editing the URL | Web report page | Normal operation | Request is refused (HTTP 403) and the attempt is logged | 0 bytes of REP data returned in 20 of 20 test requests; 20 of 20 attempts logged | (H,M) |
| QA-6 | Testability | Developer | Runs the engine tests before a commit | Report engine | Laptop, no network, in-memory sample of 3 000 rows | All tests run without files or external services and report pass/fail | `mvn test` finishes in <= 30 s (median of 5 runs); >= 80 % line coverage of the engine package | (M,L) |

## Rank justifications
- QA-1 (H,H): the report is due on the morning of day 2; 3 M rows with BigDecimal arithmetic must be parsed and aggregated within the window, which forces parallelism.
- QA-2 (M,M): branch growth is plausible but not planned; a linear design (+33 % for +33 % rows) is achievable if nothing is hard-coded to 3 branches.
- QA-3 (H,M): links fail regularly and the finance director still needs a report; it needs a clear partial-report and late-merge policy, but not exotic technology.
- QA-4 (H,L): new formats (PDF, Excel) are already planned; the Strategy pattern makes this cheap.
- QA-5 (H,M): wrong access to another branch's sales is a business and trust problem; it needs authentication and per-branch authorization in every access path.
- QA-6 (M,L): good tests protect all other qualities, and pure functions on in-memory data are easy to test.

## Assumptions
- A1: the report server has 8 cores and an SSD (to confirm with the client).
- A2: a 4th branch produces the same volume as the others (33 000 rows per day).
- A3: "late" means up to 2 hours after the 23:00 deadline; later files are handled by a manual re-run.
- A4: users are authenticated by a login system provided by head office.
- A5: release 1 produces txt, html and csv; PDF and Excel come in Lab-08.

## Utility tree
```mermaid
flowchart LR
  U((Utility)) --> P[Performance]
  U --> S[Scalability]
  U --> A[Availability]
  U --> M[Modifiability]
  U --> X[Security]
  U --> T[Testability]
  P --> QA1["QA-1 (H,H) month of 3 M rows in 60 s or less"]
  S --> QA2["QA-2 (M,M) 4th branch adds at most 35 % run time"]
  A --> QA3["QA-3 (H,M) REP file 2 h late: others out by 07:00"]
  M --> QA4["QA-4 (H,L) new export format in 2 days or less"]
  X --> QA5["QA-5 (H,M) manager sees own branch only"]
  T --> QA6["QA-6 (M,L) engine tests run in 30 s or less"]
