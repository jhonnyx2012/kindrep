---
name: project-sync
description: >-
  Coordinates Kindrep's external project tools: syncing cards on the Trello roadmap board,
  inspecting or updating Penpot design boards, and verifying Android and macOS iOS builds on GitHub Actions.
  Use when reading or updating Trello tasks, working with Penpot designs, or checking remote builds.
---

# Kindrep Project Synchronization (Trello, Penpot, GitHub Actions)

Never hardcode ephemeral card statuses in repository files; always query live state from the tools below.

## 1. Trello Roadmap Board
1. **Mandatory Prerequisite:** Always read the `trello-use` skill before calling any `trelloRead*`, `trelloWrite*`, or `trelloSearch` tools.
2. **Board Coordinates:**
   - **Board Link:** `https://trello.com/b/1oQjELR2/rehabbit-mvp`
   - **Board Identifier:** `ari:cloud:trello::board/workspace/6b046c83d0de0fd5206a4470/6b04774a2db3c1934ace2aac`
   - **List Identifiers:**
     - `📝 To Do (MVP)`: `ari:cloud:trello::list/workspace/6b046c83d0de0fd5206a4470/6b04778f8216825c59e8411b`
     - `🚧 In Progress`: `ari:cloud:trello::list/workspace/6b046c83d0de0fd5206a4470/6b0477909b1c6dbb85eadb87`
     - `🎬 Content & Video`: `ari:cloud:trello::list/workspace/6b046c83d0de0fd5206a4470/6b047792f89a2dc1a5e0f156`
     - `🔍 Review / QA`: `ari:cloud:trello::list/workspace/6b046c83d0de0fd5206a4470/6b04779394c5bd37fcc74a78`
     - `✅ Done`: `ari:cloud:trello::list/workspace/6b046c83d0de0fd5206a4470/6b047795a4fe419aa304e2d9`
     - `📋 Backlog (V1.1+)`: `ari:cloud:trello::list/workspace/6b046c83d0de0fd5206a4470/6b04778d6b99e11608e24b8d`
3. **Workflow:** Fetch current cards via `trelloReadCard` (`list_by_board`) before moving cards or posting completion links.

## 2. GitHub Repository & Remote Apple Silicon Verification
- **Repository:** `https://github.com/jhonnyx2012/kindrep`
- **Workflows (`.github/workflows/ci.yml`):** Runs on pushes and pull requests to `main` across `ubuntu-latest` (user interface architecture check, host unit tests, debug Android package) and `macos-latest` (`ComposeApp` framework + Xcode `arm64` simulator build).
- **Verify Remote Runs:** After pushing changes that affect shared code, Gradle setup, or `iosMain`/`iosApp`, verify the macOS build via:
  ```powershell
  $runIdentifier = gh run list --limit 1 --json databaseId --jq ".[0].databaseId"
  gh run watch $runIdentifier --exit-status
  ```

## 3. Penpot Design System
- **Pages:**
  - `Brand, Tokens & Mascot`: `e7ca6b81-c0d3-8029-8008-fdb04321d32f`
  - `MVP Screens - Warm Earth`: `1dddb0cb-8661-804d-8008-fdb14a8a7a60`
  - `Dark Mode - Soft Stone D3`: `1dddb0cb-8661-804d-8008-fdb14a8ba928`
- **Plugin Behavior:** Always call `await penpot.openPage(page)` before reading or mutating shapes on another page. (`File.name` is read-only in the Penpot plugin interface.)
