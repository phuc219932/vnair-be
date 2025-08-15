# VNAIR User Management API Docker Setup Script

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("start", "stop", "status", "logs", "restart", "clean")]
    [string]$Action
)

Write-Host "=== VNAIR User Management API Docker Setup ===" -ForegroundColor Cyan

# Function to check if Docker is running
function Test-Docker {
    try {
        docker info | Out-Null
        Write-Host "✅ Docker is running" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "❌ Docker is not running. Please start Docker Desktop." -ForegroundColor Red
        return $false
    }
}

# Function to build and start services
function Start-Services {
    Write-Host "🚀 Building and starting services..." -ForegroundColor Yellow
    
    # Try main Dockerfile first
    Write-Host "Attempting to build with main Dockerfile..." -ForegroundColor Cyan
    docker-compose up --build -d
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Main Dockerfile failed, trying alternative..." -ForegroundColor Yellow
        $env:DOCKERFILE = "Dockerfile.alternative"
        docker-compose up --build -d
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "❌ Both Dockerfiles failed. Please check images availability:" -ForegroundColor Red
            Write-Host "Run: ./check-images.sh" -ForegroundColor White
            exit 1
        }
    }
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Services started successfully!" -ForegroundColor Green
        Write-Host ""
        Write-Host "📋 Service URLs:" -ForegroundColor Cyan
        Write-Host "   • API: http://localhost:8080/api" -ForegroundColor White
        Write-Host "   • Database: localhost:5432" -ForegroundColor White
        Write-Host "   • Adminer: http://localhost:8081" -ForegroundColor White
        Write-Host ""
        Write-Host "🔗 Test API:" -ForegroundColor Cyan
        Write-Host "   curl http://localhost:8080/api/users/stats" -ForegroundColor White
        Write-Host ""
        Write-Host "📊 Check logs:" -ForegroundColor Cyan
        Write-Host "   docker-compose logs -f app" -ForegroundColor White
        Write-Host ""
        Write-Host "🛑 Stop services:" -ForegroundColor Cyan
        Write-Host "   .\docker-run.ps1 stop" -ForegroundColor White
    }
}

# Function to show service status
function Show-Status {
    Write-Host "📊 Service Status:" -ForegroundColor Cyan
    docker-compose ps
}

# Function to show logs
function Show-Logs {
    Write-Host "📋 Application Logs:" -ForegroundColor Cyan
    docker-compose logs -f app
}

# Function to stop services
function Stop-Services {
    Write-Host "🛑 Stopping services..." -ForegroundColor Yellow
    docker-compose down
    Write-Host "✅ Services stopped" -ForegroundColor Green
}

# Function to clean up everything
function Clean-All {
    Write-Host "🧹 Cleaning up Docker resources..." -ForegroundColor Yellow
    docker-compose down -v --rmi all
    docker system prune -f
    Write-Host "✅ Cleanup completed" -ForegroundColor Green
}

# Execute based on action parameter
switch ($Action) {
    "start" {
        if (Test-Docker) {
            Start-Services
        }
    }
    "stop" {
        Stop-Services
    }
    "status" {
        Show-Status
    }
    "logs" {
        Show-Logs
    }
    "restart" {
        if (Test-Docker) {
            Stop-Services
            Start-Services
        }
    }
    "clean" {
        Clean-All
    }
}
