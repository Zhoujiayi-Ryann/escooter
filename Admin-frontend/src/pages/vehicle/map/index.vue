<template>
  <div class="map-container">
    <div id="map" class="map"></div>
    
    <!-- 添加侧边弹窗 -->
    <t-drawer
      :visible.sync="drawerVisible"
      :header="selectedScooter ? `Vehicle Details - ${selectedScooter.scooterCode}` : 'Vehicle Details'"
      :footer="false"
      :size="'400px'"
      :close-btn="true"
      :on-close="onDrawerClose"
    >
      <div v-if="selectedScooter" class="scooter-details">
        <t-descriptions :column="1" bordered>
          <t-descriptions-item label="Vehicle No.">{{ selectedScooter.scooterCode }}</t-descriptions-item>
          <t-descriptions-item label="Status">
            <t-tag v-if="selectedScooter.status === 0" theme="success" variant="light">Free</t-tag>
            <t-tag v-if="selectedScooter.status === 1" theme="primary" variant="light">Booked</t-tag>
            <t-tag v-if="selectedScooter.status === 2" theme="warning" variant="light">In Use</t-tag>
            <t-tag v-if="selectedScooter.status === 3" theme="danger" variant="light">Maintenance</t-tag>
          </t-descriptions-item>
          <t-descriptions-item label="Battery">
            <t-progress :percentage="selectedScooter.battery" :color="getBatteryColor(selectedScooter.battery)" :label="false" trackColor="#e8f4ff" />
            <span style="margin-left: 4px">{{ selectedScooter.battery }}%</span>
          </t-descriptions-item>
          <t-descriptions-item label="Price">£{{ selectedScooter.price }} / min</t-descriptions-item>
          <t-descriptions-item label="Location">{{ selectedScooter.location }}</t-descriptions-item>
          <t-descriptions-item label="Last Rental Time">{{ selectedScooter.lastRentTime || '-' }}</t-descriptions-item>
        </t-descriptions>
      </div>
    </t-drawer>
  </div>
</template>

<script>
import Vue from 'vue';
import { scooterService } from '@/service/service-scooter';
import { 
  Drawer as TDrawer,
  Descriptions as TDescriptions,
  DescriptionsItem as TDescriptionsItem,
  Tag as TTag,
  Progress as TProgress
} from 'tdesign-vue';

export default {
  name: 'ScooterMap',
  components: {
    TDrawer,
    TDescriptions,
    TDescriptionsItem,
    TTag,
    TProgress
  },
  data() {
    return {
      map: null,
      markers: [],
      center: { lat: 39.909, lng: 116.39742 },
      zoom: 13,
      drawerVisible: false,
      selectedScooter: null,
      allScooters: [], // 添加一个变量来存储所有滑板车数据
    };
  },
  methods: {
    initMap() {
      // 从路由参数中获取经纬度
      const lat = this.$route.query.lat ? Number(this.$route.query.lat) : 39.909;
      const lng = this.$route.query.lng ? Number(this.$route.query.lng) : 116.39742;
      
      this.map = new TMap.Map('map', {
        center: new TMap.LatLng(lat, lng),
        zoom: this.$route.query.lat && this.$route.query.lng ? 15 : 12,
        key: '4HZBZ-FLRCL-COFPO-EKA57-6CILQ-OEFTJ',
        // 添加地图控件
        control: {
          // 添加缩放控件
          zoomControl: {
            position: 'TOP_RIGHT',
            zoomInText: '+',
            zoomOutText: '-',
            zoomInTitle: '放大',
            zoomOutTitle: '缩小'
          },
          // 添加旋转控件
          rotationControl: {
            position: 'TOP_RIGHT'
          },
          // 添加比例尺控件
          scaleControl: {
            position: 'BOTTOM_LEFT'
          }
        },
        // 启用双击缩放
        doubleClickZoom: true,
        // 启用滚轮缩放
        scrollWheel: true
      });
    },

    async loadScooters() {
      try {
        const scooters = await scooterService.getAllScooters();
        this.allScooters = scooters; // 保存所有滑板车数据
        this.markers.forEach(marker => marker.setMap(null));
        this.markers = [];

        // 创建标记样式
        const markerStyle = new TMap.MarkerStyle({
          width: 20,
          height: 20,
          anchor: { x: 10, y: 10 },
          src: 'https://mapapi.qq.com/web/lbs/javascriptGL/demo/img/markerDefault.png'
        });

        scooters.forEach(scooter => {
          const marker = new TMap.MultiMarker({
            map: this.map,
            styles: {
              marker: markerStyle
            },
            geometries: [{
              id: scooter.id.toString(),
              position: new TMap.LatLng(
                parseFloat(scooter.location.split('(')[1].split(',')[0]),
                parseFloat(scooter.location.split(',')[1].split(')')[0])
              ),
              properties: {
                title: '滑板车编号: ' + scooter.scooterCode + '\n电量: ' + scooter.battery + '%\n价格: ¥' + scooter.price + '/小时'
              }
            }]
          });

          // 添加点击事件
          marker.on('click', (evt) => {
            const clickedScooter = scooters.find(s => s.id.toString() === evt.geometry.id);
            if (clickedScooter) {
              this.selectedScooter = clickedScooter;
              this.drawerVisible = true;
              console.log('Selected scooter:', clickedScooter);
            }
          });

          this.markers.push(marker);
        });

        // 如果有经纬度参数，自动打开对应车辆的侧边栏
        if (this.$route.query.lat && this.$route.query.lng) {
          const targetLat = Number(this.$route.query.lat);
          const targetLng = Number(this.$route.query.lng);
          
          // 找到最接近的滑板车
          const targetScooter = scooters.find(scooter => {
            const [lat, lng] = scooter.location.match(/\(([^,]+),\s*([^)]+)\)/).slice(1).map(Number);
            return Math.abs(lat - targetLat) < 0.0001 && Math.abs(lng - targetLng) < 0.0001;
          });

          if (targetScooter) {
            this.selectedScooter = targetScooter;
            this.drawerVisible = true;
          }
        }
      } catch (error) {
        console.error('加载滑板车数据失败:', error);
      }
    },

    onLoad(options) {
      // 如果有传入经纬度参数，则使用传入的经纬度作为地图中心
      if (options.lat && options.lng) {
        this.center = {
          lat: parseFloat(options.lat),
          lng: parseFloat(options.lng)
        };
        this.zoom = 15; // 放大到更近的级别
      }
    },

    onDrawerClose() {
      this.drawerVisible = false;
      this.selectedScooter = null;
    },

    getBatteryColor(battery) {
      if (battery <= 20) {
        return '#e34d59';
      }
      if (battery <= 50) {
        return '#ed7b2f';
      }
      return '#00a870';
    }
  },
  mounted() {
    const script = document.createElement('script');
    script.src = 'https://map.qq.com/api/gljs?v=1.exp&key=4HZBZ-FLRCL-COFPO-EKA57-6CILQ-OEFTJ';
    script.onload = () => {
      this.initMap();
      this.loadScooters();
    };
    document.head.appendChild(script);
  }
};
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 100%;
  position: relative;
}

.map {
  width: 100%;
  height: calc(100vh - 64px); /* 减去顶部导航栏的高度 */
}

.scooter-details {
  padding: 16px;
}

:deep(.t-descriptions) {
  margin-top: 16px;
}

:deep(.t-descriptions__label) {
  width: 120px;
}
</style>