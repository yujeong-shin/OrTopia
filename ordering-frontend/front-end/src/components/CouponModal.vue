<template>
  <v-dialog v-model="dialog" max-width="600px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        나의 쿠폰
      </v-card-title>
      <v-card-text>
        <v-list dense>
          <v-list-item-group>
            <v-list-item v-for="item in coupons" :key="item.id">
              <v-list-item-content>
                <v-list-item-title>{{ item.couponName }}</v-list-item-title>
                <v-list-item-subtitle>유효 기간: {{ item.startDate }} - {{ item.endDate }}</v-list-item-subtitle>
                <v-list-item-subtitle v-if="item.discount > 0">
                  할인: {{ item.discount }}{{ item.rateDiscount ? '%' : '원' }}
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
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member/coupons`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "X-Refresh-Token": `${refreshToken}`,
          },
        });
        this.coupons = response.data.result;
        console.log("Loaded coupons:", this.coupons);
      } catch (e) {
        console.error("Failed to fetch coupons:", e);
        alert("쿠폰 데이터를 불러오는 데 실패했습니다.");
      }
    }
  }
};
</script>