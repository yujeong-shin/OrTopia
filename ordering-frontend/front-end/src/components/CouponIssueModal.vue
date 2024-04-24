<template>
    <v-dialog v-model="show" max-width="600px">
      <v-card>
        <v-card-title></v-card-title>
        <v-card-text>
          <v-radio-group v-model="selectedCouponId">
            <v-radio
              v-for="coupon in coupons"
              :key="coupon.id"
              :label="`${coupon.name} (${coupon.startDate} - ${coupon.endDate}) 할인율: ${coupon.rateDiscount}% 정액할인: ${coupon.fixDiscount}원`"
              :value="coupon.id"
            ></v-radio>
          </v-radio-group>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="close">취소</v-btn>
          <v-btn color="green darken-1" text @click="issueCoupon">쿠폰 발급</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    props: ['email', 'show'],
    data() {
      return {
        coupons: [],
        selectedCouponId: null
      };
    },
    methods: {
      fetchCoupons() {
        const token = localStorage.getItem("accessToken");
        axios.get(`${process.env.VUE_APP_API_BASE_URL}/coupondetail`, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'myEmail': this.email
          }
        })
        .then(response => {
          this.coupons = response.data.result.map(coupon => ({
            ...coupon,
            label: `${coupon.name} (${coupon.startDate} - ${coupon.endDate}) 할인율: ${coupon.rateDiscount}% 정액할인: ${coupon.fixDiscount}원`
          }));
        })
        .catch(error => {
          console.error('Error fetching coupons:', error);
        });
      },
      issueCoupon() {
        this.$emit('issue-coupon', this.selectedCouponId);
        this.close();
      },
      close() {
        this.$emit('update:show', false);
      }
    },
    watch: {
      show(newVal) {
        if (newVal) {
          this.fetchCoupons();
        }
      }
    }
  };
  </script>