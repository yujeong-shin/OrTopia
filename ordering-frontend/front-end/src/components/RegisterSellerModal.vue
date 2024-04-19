<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1000px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        판매자등록
      </v-card-title>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-card>
          <v-card-title class="text-h5">판매자 등록</v-card-title>
          <v-card-text>
            <v-form ref="form" @submit.prevent="submitSellerForm" v-model="valid">
              <v-text-field
                label="사업자 번호"
                v-model="sellerForm.businessNumber"
                :rules="businessNumberRules"
                required
              ></v-text-field>

              <v-text-field
                label="회사명"
                v-model="sellerForm.companyName"
                :rules="companyNameRules"
                required
              ></v-text-field>

              <v-select
                label="사업 유형"
                v-model="sellerForm.businessType"
                :items="['가구', '도서','가전','생활','건강','스포츠','식품','육아','의류','잡화','화장품']"
                :rules="businessTypeRules"
                required
              ></v-select>

              <v-btn type="submit" color="success" :disabled="!valid">등록하기</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from 'axios';
export default {
  created() {},
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      valid: true,
      sellerForm: {
        businessNumber: '',
        companyName: '',
        businessType: ''
      },
      businessNumberRules: [
        v => !!v || '사업자 번호는 필수입니다.',
        v => /^\d{10}$/.test(v) || '사업자 번호는 10자리 숫자여야 합니다.'
      ],
      companyNameRules: [
        v => !!v || '회사명은 필수입니다.'
      ],
      businessTypeRules: [
        v => !!v || '사업 유형은 필수입니다.'
      ], 
    };
  },
  methods: {
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
    async submitSellerForm() {
      console.log('판매자 등록 시도:', this.sellerForm); // 현재 입력된 데이터 확인
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem('email');
      if (this.sellerForm.businessNumber && this.sellerForm.companyName && this.sellerForm.businessType) {
        try {
          console.log('판매자 등록 요청을 보냅니다...'); // 요청 전송 전에 로그 추가
          const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/seller/${email}/create`, {
            businessNumber: this.sellerForm.businessNumber,
            companyName: this.sellerForm.companyName,
            businessType: this.sellerForm.businessType
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
              'Content-Type': 'application/json',
              myEmail : `${email}`
            }
          });
          console.log('Seller registered successfully', response.data);
          localStorage.setItem('role', 'SELLER');
          alert('판매자 등록 성공!');
          window.location.href =  "/";
        // this.$router.push('/sell');
        //  새로고침을 하지 않으면 제대로 반영이 안되서 사용자 등록이 남네요... ㅜㅜ 추후 보완필요
        } catch (error) {
          console.error('판매자 등록 오류:', error); // 오류 로그 추가
          alert(`등록 실패: ${error.response.data.message}`);
        }
      } else {
        alert('모든 필드를 채워주세요');
      }
    },
  },
};
</script>
