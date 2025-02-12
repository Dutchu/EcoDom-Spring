# Path to your .secrets file (adjust the path if needed)
$secretsFile = ".secrets"

# Verify that the file exists
if (-not (Test-Path $secretsFile)) {
    Write-Error "File '$secretsFile' not found."
    exit 1
}

# Read each line of the .secrets file
$lines = Get-Content $secretsFile

foreach ($line in $lines) {
    # Trim whitespace from the line
    $trimmedLine = $line.Trim()

    # Skip empty lines or lines starting with '#' (if you use comments)
    if ([string]::IsNullOrWhiteSpace($trimmedLine) -or $trimmedLine.StartsWith("#")) {
        continue
    }

    # Split the line into key and value at the first '=' character
    $parts = $trimmedLine -split '=', 2

    if ($parts.Count -eq 2) {
        $key = $parts[0].Trim()
        $value = $parts[1].Trim()

        # Optionally, remove surrounding quotes from the value (if present)
        if ($value.StartsWith('"') -and $value.EndsWith('"')) {
            $value = $value.Trim('"')
        }

        # Set the environment variable for the current process
        [System.Environment]::SetEnvironmentVariable($key, $value, "Process")

        Write-Host "Set environment variable '$key'"
    }
    else {
        Write-Warning "Skipping invalid line: $line"
    }
}
