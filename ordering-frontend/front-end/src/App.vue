<template>
  <v-app style="font-size: 16px">
    <v-main>
      <HeaderComponent />
      <router-view></router-view>
      <FooterComponent />
    </v-main>
  </v-app>
</template>

<script>
import HeaderComponent from "./components/HeaderComponent.vue";
import FooterComponent from "./components/FooterComponent.vue";

export default {
  name: "App",

  components: {
    HeaderComponent,
    FooterComponent,
  },

  data: () => ({
    //
  }),
};
</script>
<style>
@font-face {
  font-family: "jua";
  src: url(../public/font/BMJUA_ttf.ttf);
}

* {
  font-family: "jua";
  font-size: 17px; /* 전체 글꼴 크기 설정 */
}
</style>
