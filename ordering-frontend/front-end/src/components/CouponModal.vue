<template>
  <v-dialog v-model="dialog" max-width="600px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">나의 쿠폰</v-card-title>
      <v-card-text>
        <v-list dense>
          <v-list-item-group>
            <v-list-item v-for="item in coupons" :key="item.id">
              <v-list-item-content>
                <v-list-item-title>{{ item.couponName }}</v-list-item-title>
                <v-list-item-subtitle>{{ item.itemId }}</v-list-item-subtitle>
                <v-list-item-subtitle>유효 기간: {{ item.startDate }} - {{ item.endDate }}</v-list-item-subtitle>
                <v-list-item-subtitle v-if="item.fixDiscount > 0">
                  할인: {{ item.fixDiscount }}원
                </v-list-item-subtitle>
                <v-list-item-subtitle v-else-if="item.rateDiscount > 0">
                  할인: {{ item.rateDiscount }}%
                </v-list-item-subtitle>
                <v-btn @click="selectCoupon(item)" :disabled="!canApplyCoupon(item)">적용</v-btn>
              </v-list-item-content>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="dialog = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
import axios from 'axios';

export default {
  props: {
    modelValue: Boolean,
    myItems: Array
  },
  emits: ['update:modelValue'],
  data() {
    return {
      dialog: this.modelValue,
      coupons: []
    };
  },
  watch: {
    modelValue(newVal) {
      this.dialog = newVal;
    },
    dialog(newVal) {
      this.$emit('update:modelValue', newVal);
      if (newVal) {
        this.fetchCoupons();
      }
    }
  },
  methods: {
    selectCoupon(coupon) {
      this.$emit('select-coupon', coupon);
      this.dialog = false;
    },
    async fetchCoupons() {
  const token = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");
  try {
    const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member/coupons`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Refresh-Token": `${refreshToken}`,
      },
    });
    this.coupons = response.data.result;
    console.log("Loaded coupons:", this.coupons);  // 쿠폰 데이터 확인을 위한 로그 출력
  } catch (e) {
    console.error("Failed to fetch coupons:", e);
    alert("사용가능한 쿠폰이 없습니다.");
  }
},
canApplyCoupon(coupon) {
      // 쿠폰의 itemId가 myItems 배열의 어떤 아이템의 id와 일치하는지 확인
      return this.myItems.some(item => item.id === coupon.itemId);
    }
  }
};
</script>