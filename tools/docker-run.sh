#!/bin/bash

echo "=== VNAIR User Management API Docker Setup ==="

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo "❌ Docker is not running. Please start Docker Desktop."
        exit 1
    fi
    echo "✅ Docker is running"
}

# Function to build and start services
start_services() {
    echo "🚀 Building and starting services..."
    docker-compose up --build -d
    
    if [ $? -eq 0 ]; then
        echo "✅ Services started successfully!"
        echo ""
        echo "📋 Service URLs:"
        echo "   • API: http://localhost:8080/api"
        echo "   • Database: localhost:5432"
        echo "   • Adminer: http://localhost:8081"
        echo ""
        echo "🔗 Test API:"
        echo "   curl http://localhost:8080/api/users/stats"
        echo ""
        echo "📊 Check logs:"
        echo "   docker-compose logs -f app"
        echo ""
        echo "🛑 Stop services:"
        echo "   docker-compose down"
    else
        echo "❌ Failed to start services"
        exit 1
    fi
}

# Function to show service status
show_status() {
    echo "📊 Service Status:"
    docker-compose ps
}

# Function to show logs
show_logs() {
    echo "📋 Application Logs:"
    docker-compose logs -f app
}

# Function to stop services
stop_services() {
    echo "🛑 Stopping services..."
    docker-compose down
    echo "✅ Services stopped"
}

# Function to clean up everything
clean_all() {
    echo "🧹 Cleaning up Docker resources..."
    docker-compose down -v --rmi all
    docker system prune -f
    echo "✅ Cleanup completed"
}

# Parse command line arguments
case "$1" in
    "start")
        check_docker
        start_services
        ;;
    "stop")
        stop_services
        ;;
    "status")
        show_status
        ;;
    "logs")
        show_logs
        ;;
    "restart")
        check_docker
        stop_services
        start_services
        ;;
    "clean")
        clean_all
        ;;
    *)
        echo "Usage: $0 {start|stop|status|logs|restart|clean}"
        echo ""
        echo "Commands:"
        echo "  start   - Build and start all services"
        echo "  stop    - Stop all services"
        echo "  status  - Show service status"
        echo "  logs    - Show application logs"
        echo "  restart - Restart all services"
        echo "  clean   - Clean up all Docker resources"
        exit 1
        ;;
esac
