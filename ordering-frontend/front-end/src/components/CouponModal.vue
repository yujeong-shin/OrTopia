<template>
  <v-dialog v-model="dialog" max-width="600px">
    <v-card>
      <v-card-title class="text-h5">나의 쿠폰</v-card-title>
      <v-card-text>
        <v-list dense>
          <v-list-item-group>
            <v-list-item v-for="item in coupons" :key="item.id">
              <v-list-item-content>
                <v-list-item-title>{{ item.couponDetail.name }}</v-list-item-title>
                <v-list-item-subtitle>유효 기간: {{ item.couponDetail.startDate }} - {{ item.couponDetail.endDate }}</v-list-item-subtitle>
                <v-list-item-subtitle v-if="item.couponDetail.rateDiscount > 0">
                  할인율: {{ item.couponDetail.rateDiscount }}%
                </v-list-item-subtitle>
                <v-list-item-subtitle v-if="item.couponDetail.fixDiscount > 0">
                  할인액: {{ item.couponDetail.fixDiscount }}원
                </v-list-item-subtitle>
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
    modelValue: Boolean
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
    async fetchCoupons() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/mycoupons`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "X-Refresh-Token": `${refreshToken}`,
          },
        });
        this.coupons = response.data.result; // 서버 응답에 따라 결과 배열을 저장
        console.log("Loaded coupons:", this.coupons);
      } catch (e) {
        console.error("Failed to fetch coupons:", e);
        alert("쿠폰 데이터를 불러오는 데 실패했습니다.");
      }
    }
  }
};
</script>