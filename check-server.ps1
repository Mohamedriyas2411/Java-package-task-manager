Write-Host "Checking server status..."

$endpoints = @("/", "/api/system-info", "/api/currentTime")

foreach ($endpoint in $endpoints) {
    $url = "http://localhost:8080$endpoint"
    try {
        Write-Host "Testing $url..."
        $response = Invoke-WebRequest -Uri $url -Method GET -TimeoutSec 5 -ErrorAction Stop
        Write-Host "SUCCESS! Status code: $($response.StatusCode)"
        Write-Host "Response content type: $($response.Headers["Content-Type"])"
        Write-Host "Response first 100 chars: $($response.Content.Substring(0, [Math]::Min(100, $response.Content.Length)))..."
        Write-Host ""
    } catch {
        Write-Host "FAILED! Error: $_"
        Write-Host ""
    }
}

Write-Host "Server check complete." 