# C4 Diagram Notes

## Level 1 — System Context

The context diagram deliberately omits internal implementation details, database structure, and the steps used to calculate monthly summaries. The system supports QA-1 (performance) by generating monthly reports from branch data, while branch-specific access supports QA-5 (security). An open question for the client is whether the finance director should receive a notification only when all three branches have submitted their files or also when a partial report is available.

## Level 2 — Container Diagram

The container diagram deliberately omits Java classes, individual methods, database tables, and detailed deployment infrastructure. The CSV landing zone and ingest job support QA-3 (availability) by allowing incoming branch files to be collected and processed independently, while the web report page supports QA-5 (security) by restricting branch managers to their own branch's reports. An open question for the client is which relational database and authentication mechanism should be used for the first release.