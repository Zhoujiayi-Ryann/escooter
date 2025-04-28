<template>
  <div class="map-container">
    <div id="map" class="map"></div>
  </div>
</template>

<script>
import Vue from 'vue';
import { scooterService } from '@/service/service-scooter';

export default {
  name: 'ScooterMap',
  data() {
    return {
      map: null,
      markers: [],
      center: { lat: 39.909, lng: 116.39742 },
      zoom: 13,
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
        key: '4HZBZ-FLRCL-COFPO-EKA57-6CILQ-OEFTJ'
      });
    },

    async loadScooters() {
      try {
        const scooters = await scooterService.getAllScooters();
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

          this.markers.push(marker);
        });
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
</style>