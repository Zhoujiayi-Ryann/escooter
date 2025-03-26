#!/bin/bash

# 切换数据库环境的脚本

# 获取当前工作目录
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
CONFIG_FILE="$DIR/src/main/resources/application.properties"

# 检查参数
if [ "$1" == "local" ]; then
    echo "切换到本地数据库环境..."
    # 使用sed替换active profile配置
    sed -i 's/^spring\.profiles\.active=.*$/# spring.profiles.active=prod\nspring.profiles.active=local/' "$CONFIG_FILE"
    echo "已切换到本地数据库环境。现在您将使用localhost:3306的MySQL数据库。"
elif [ "$1" == "cloud" ]; then
    echo "切换到云端数据库环境..."
    # 使用sed替换active profile配置
    sed -i 's/^spring\.profiles\.active=.*$/# spring.profiles.active=local\nspring.profiles.active=prod/' "$CONFIG_FILE"
    echo "已切换到云端数据库环境。现在您将使用escoot-mysql.ns-x3h6hlzn.svc:3306的MySQL数据库。"
else
    echo "用法: ./switch-db.sh [local|cloud]"
    echo "  local - 切换到本地数据库"
    echo "  cloud - 切换到云端数据库"

    # 显示当前环境
    CURRENT_ENV=$(grep "^spring.profiles.active=" "$CONFIG_FILE" | cut -d= -f2)
    echo "当前环境: $CURRENT_ENV"
fi

echo "提示: 切换环境后，需要重启应用才能生效。" 