#!/bin/bash

# 电动滑板车租赁系统 - 数据库隧道设置脚本
# 此脚本帮助您在本地机器上设置SSH隧道，连接到集群内部的MySQL数据库

# 配置参数 - 请根据实际情况修改
JUMP_SERVER="[填写您的集群跳板机地址]"
JUMP_USER="[填写您的跳板机用户名]"
DB_HOST="escoot-mysql.ns-x3h6hlzn.svc"
DB_PORT="3306"
LOCAL_PORT="3306"

# 显示帮助信息
show_help() {
    echo "电动滑板车租赁系统 - 数据库隧道设置脚本"
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  -h, --help              显示此帮助信息"
    echo "  -s, --server SERVER     设置跳板机地址 (默认: $JUMP_SERVER)"
    echo "  -u, --user USER         设置跳板机用户名 (默认: $JUMP_USER)"
    echo "  -d, --db-host HOST      设置数据库主机名 (默认: $DB_HOST)"
    echo "  -p, --db-port PORT      设置数据库端口 (默认: $DB_PORT)"
    echo "  -l, --local-port PORT   设置本地映射端口 (默认: $LOCAL_PORT)"
    echo ""
    echo "示例:"
    echo "  $0 -s jump.example.com -u john -l 13306"
    echo ""
}

# 处理命令行参数
while [[ "$#" -gt 0 ]]; do
    case $1 in
        -h|--help) show_help; exit 0 ;;
        -s|--server) JUMP_SERVER="$2"; shift ;;
        -u|--user) JUMP_USER="$2"; shift ;;
        -d|--db-host) DB_HOST="$2"; shift ;;
        -p|--db-port) DB_PORT="$2"; shift ;;
        -l|--local-port) LOCAL_PORT="$2"; shift ;;
        *) echo "未知参数: $1"; show_help; exit 1 ;;
    esac
    shift
done

# 检查必要参数
if [ -z "$JUMP_SERVER" ] || [ "$JUMP_SERVER" = "[填写您的集群跳板机地址]" ]; then
    echo "错误: 请提供跳板机地址"
    show_help
    exit 1
fi

if [ -z "$JUMP_USER" ] || [ "$JUMP_USER" = "[填写您的跳板机用户名]" ]; then
    echo "错误: 请提供跳板机用户名"
    show_help
    exit 1
fi

# 显示配置信息
echo "================================================"
echo "电动滑板车租赁系统 - 数据库隧道设置"
echo "================================================"
echo "跳板机地址:     $JUMP_USER@$JUMP_SERVER"
echo "数据库主机:     $DB_HOST:$DB_PORT"
echo "本地映射端口:   localhost:$LOCAL_PORT"
echo "================================================"
echo "建立隧道后，请使用以下数据库连接配置："
echo "URL: jdbc:mysql://localhost:$LOCAL_PORT/escoot"
echo "================================================"
echo "按Ctrl+C可以终止隧道连接"
echo "================================================"
echo "正在连接..."

# 建立SSH隧道
ssh -L $LOCAL_PORT:$DB_HOST:$DB_PORT $JUMP_USER@$JUMP_SERVER -N -v

# 如果SSH连接断开，显示提示信息
echo ""
echo "SSH隧道连接已断开"
echo "请修改 application-dev.properties 中的数据库URL:"
echo "spring.datasource.url=jdbc:mysql://localhost:$LOCAL_PORT/escoot?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
echo "" 