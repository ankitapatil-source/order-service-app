#!/usr/bin/env bash
#
# seed-git-history.sh
#
# Initialises this project as a Git repository with a small, realistic commit
# history (instead of one big "Initial commit"). This gives you something to
# explore in Assignment 2 — recent changes, which commit last touched a file,
# and how the codebase grew.
#
# Usage (from the project root):
#   bash scripts/seed-git-history.sh
#
# Then add your GitHub repo as the remote and push:
#   git remote add origin <YOUR_REPO_SSH_OR_HTTPS_URL>
#   git push -u origin main
#
set -euo pipefail

cd "$(dirname "$0")/.."

if [ -d .git ]; then
  echo "This project already has a .git directory. Remove it first if you want to re-seed."
  exit 1
fi

git init -q
git checkout -q -b main 2>/dev/null || git branch -q -M main

commit() {
  # $1 = date (YYYY-MM-DD), $2 = message, rest = paths to add
  local date="$1"; shift
  local msg="$1"; shift
  git add "$@"
  GIT_AUTHOR_DATE="${date}T10:00:00" GIT_COMMITTER_DATE="${date}T10:00:00" \
    git commit -q -m "$msg"
  echo "  committed: $msg"
}

echo "Seeding commit history..."
commit 2026-07-14 "Project scaffold: Maven build, README, gitignore" \
  pom.xml .gitignore README.md logs/.gitkeep
commit 2026-07-15 "Add domain model (product, customer, coupon, order)" \
  src/main/java/com/rsl/orderservice/model
commit 2026-07-16 "Add in-memory repositories" \
  src/main/java/com/rsl/orderservice/repository
commit 2026-07-17 "Add logging setup" \
  src/main/java/com/rsl/orderservice/util
commit 2026-07-18 "Add pricing and inventory services" \
  src/main/java/com/rsl/orderservice/service/PricingService.java \
  src/main/java/com/rsl/orderservice/service/InventoryService.java
commit 2026-07-21 "Add discount/coupon logic and order flow" \
  src/main/java/com/rsl/orderservice/service/DiscountService.java \
  src/main/java/com/rsl/orderservice/service/OrderService.java
commit 2026-07-22 "Add application entry point with sample scenarios" \
  src/main/java/com/rsl/orderservice/App.java
commit 2026-07-23 "Add unit tests" \
  src/test

echo
echo "Done. Commit history:"
git --no-pager log --oneline
echo
echo "Next: add your GitHub remote and push, e.g."
echo "  git remote add origin <YOUR_REPO_URL>"
echo "  git push -u origin main"
