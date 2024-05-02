<template>
  <v-dialog v-model="show" persistent max-width="600px">
    <v-card>
      <v-card-title class="headline">쿠폰 정보 입력</v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12">
              <v-text-field v-model="details.name" label="쿠폰 이름"></v-text-field>
            </v-col>
            <v-col cols="12">
              <input type="date" v-model="details.startDate" class="date-input">
            </v-col>
            <v-col cols="12">
              <input type="date" v-model="details.endDate" class="date-input">
            </v-col>
            <v-col cols="12">
              <v-text-field v-model="details.firstCome" label="선착순 인원 (명)" type="number"></v-text-field>
            </v-col>
            <v-row>
              <v-col cols="12"></v-col>
              <v-col cols="12">
                <v-btn color="primary" @click="setActiveDiscountType('rate')">%</v-btn>
                <v-text-field
                  v-model="details.rateDiscount"
                  label="할인율 (%)"
                  type="number"
                  :disabled="activeDiscountType !== 'rate'"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-btn color="secondary" @click="setActiveDiscountType('fix')">원</v-btn>
                <v-text-field
                  v-model="details.fixDiscount"
                  label="정액 할인 (원)"
                  type="number"
                  :disabled="activeDiscountType !== 'fix'"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="cancel">취소</v-btn>
        <v-btn color="blue darken-1" text @click="create">생성</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from 'axios';

export default {
props: {
  value: Boolean,
},
data() {
  return {
    show: false,
    activeDiscountType: null,
    startMenu: false,
    endMenu: false,
    details: {
      name: '',
      startDate: '',
      endDate: '',
      firstCome: 0, // 선착순 인원 데이터 추가
      rateDiscount: 0,
      fixDiscount: 0
    }
  };
},
watch: {
  value(newVal) {
    this.show = newVal;
  }
},
methods: {
  setActiveDiscountType(type) {
  this.activeDiscountType = type;
  if (type === 'rate') {
    this.details.fixDiscount = 0; // 정액 할인 초기화
  } else {
    this.details.rateDiscount = 0; // 할인율 초기화
  }
},
  cancel() {
    this.$emit('update:value', false);
  },
  create() {
    const token = localStorage.getItem("accessToken");
    const refreshToken = localStorage.getItem("refreshToken");
    const email = localStorage.getItem("email");
    const couponData = this.details;

    axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/coupondetail/create`, couponData, {
      headers: {
        Authorization: `Bearer ${token}`,
        "X-Refresh-Token": `${refreshToken}`,
        "Content-Type": "application/json",
        myEmail: `${email}`,
      }
    })
    .then(response => {
      console.log('Coupon created successfully:', response);
      this.cancel();
      this.$emit('created');
      alert('쿠폰 생성이 완료되었습니다.');
    })
    .catch(error => {
      console.error('Error creating coupon:', error);
      alert('쿠폰 생성에 실패했습니다.');
    });
  }
}
};
</script>

<style scoped>
.date-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
</style>