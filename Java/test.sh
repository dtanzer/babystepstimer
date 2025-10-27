#!/bin/bash

# Run tests with --rerun-tasks to force execution even without code changes
# Capture output and exit code
output=$(./gradlew test --rerun-tasks 2>&1)
exit_code=$?

# Check if tests passed
if [ $exit_code -eq 0 ]; then
    # Extract test count from test-logger output (look for "N passing")
    test_count=$(echo "$output" | grep -oE '[0-9]+ passing' | tail -1 | grep -oE '^[0-9]+')
    
    # Default to "All" if count not found
    if [ -z "$test_count" ]; then
        echo "All Tests succeeded"
    else
        echo "$test_count Tests succeeded"
    fi
else
    # Tests failed - show detailed output
    echo "$output"
fi

exit $exit_code