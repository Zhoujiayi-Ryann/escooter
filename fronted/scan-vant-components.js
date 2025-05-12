const fs = require('fs');
const path = require('path');

// 要扫描的目录（可自行扩展）
const dirsToScan = [
  path.resolve(__dirname, './pages'),
  
  path.resolve(__dirname, './')  // 保险起见扫一下根目录
];

const vantTags = new Set();

function walk(dir) {
  const files = fs.readdirSync(dir);
  for (const file of files) {
    const fullPath = path.join(dir, file);
    const stat = fs.statSync(fullPath);
    if (stat.isDirectory()) {
      walk(fullPath);
    } else if (file.endsWith('.vue') || file.endsWith('.uvue')) {
      const content = fs.readFileSync(fullPath, 'utf-8');
      const matches = content.match(/<van-[\w-]+/g);
      if (matches) {
        matches.forEach(tag => vantTags.add(tag.slice(1)));
      }
    }
  }
}

dirsToScan.forEach(walk);

// 输出结果
const tagList = [...vantTags];
if (tagList.length === 0) {
  console.log('❌ 没有检测到 van-组件使用');
  process.exit(0);
}

function toPascalCase(str) {
  return str
    .replace(/^van-/, '')
    .split('-')
    .map(s => s.charAt(0).toUpperCase() + s.slice(1))
    .join('');
}

console.log('✅ 检测到以下 Vant 组件：\n');
tagList.forEach(tag => console.log(`<${tag}>`));

console.log('\n📦 对应 import：\n');
tagList.forEach(tag => {
  const comp = toPascalCase(tag);
  console.log(`import { ${comp} } from 'vant'`);
});

console.log('\n🔧 在 components 中注册：\ncomponents: {');
tagList.forEach(tag => {
  const comp = toPascalCase(tag);
  console.log(`  '${tag}': ${comp},`);
});
console.log('}');
