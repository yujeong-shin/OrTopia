<template>
  <v-dialog v-model="dialog" max-width="1000px">
    <v-card>
      <v-card-title class="text-center">
        <span class="text-h5 grey lighten-2">판매자 등록</span>
      </v-card-title>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6">
          <v-card>
            <v-card-title>판매자 등록</v-card-title>
            <v-card-text>
              <v-form ref="form" @submit.prevent="submitSellerForm">
                <v-text-field
                  label="사업자 번호"
                  v-model="sellerForm.businessNumber"
                  :rules="businessNumberRules"
                  :disabled="validBusinessNumber"
                  required
                ></v-text-field>
                <v-btn color="primary" @click="checkBusinessNumber">사업자 번호 검증</v-btn>
                <v-text-field
                  label="회사명"
                  v-model="sellerForm.companyName"
                  :rules="companyNameRules"
                  required
                ></v-text-field>
                <v-select
                  label="사업 유형"
                  v-model="sellerForm.businessType"
                  :items="businessTypes"
                  :rules="businessTypeRules"
                  required
                ></v-select>
                <v-btn type="submit" color="success" :disabled="!valid || !validBusinessNumber">
                  등록하기
                </v-btn>
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
    <!-- 추가된 확인 Dialog -->
    <v-dialog v-model="showConfirmDialog" persistent>
      <v-card>
        <v-card-title class="text-h5">검증 완료</v-card-title>
        <v-card-text>사업자 번호가 유효합니다. 계속하려면 확인을 누르세요.</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="confirmBusinessNumber">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-dialog>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      dialog: false,
      showConfirmDialog: false,
      valid: true,
      sellerForm: {
        businessNumber: '',
        companyName: '',
        businessType: '',
      },
      validBusinessNumber: false,
      businessNumberRules: [
        v => !!v || '사업자 번호는 필수입니다.',
        v => /^\d{10}$/.test(v) || '사업자 번호는 10자리 숫자여야 합니다.',
      ],
      companyNameRules: [v => !!v || '회사명은 필수입니다.'],
      businessTypeRules: [v => !!v || '사업 유형은 필수입니다.'],
      businessTypes: ['가구', '도서', '가전', '생활', '건강', '스포츠', '식품', '육아', '의류', '잡화', '화장품'],
    };
  },
  methods: {
    updateDialog(value) {
      this.$emit('update:dialog', value);
    },
    close() {
      this.$emit('update:dialog', false);
    },
    async submitSellerForm() {
      // 등록 로직
    },
    async checkBusinessNumber() {
      const data = { 'b_no': [this.sellerForm.businessNumber] };
      try {
        const response = await axios.post(
          `https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=crGL2N15kbZz5KnajRhQC4Pur3KT5XFRX%2BBLM6DB4XNqet1Kp54PUOYMS2UrNAfdMkvD%2FXKV7FOTrcoRhvdtQQ%3D%3D`,
          JSON.stringify(data),
          {
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
            },
          }
        );
        if (response.data.data[0].b_stt_cd === '01') {
          this.showConfirmDialog = true;
        } else {
          alert('사업자 번호가 유효하지 않습니다.');
        }
      } catch (error) {
        console.error('API 호출 오류:', error);
        alert('사업자 번호 검증 중 오류가 발생했습니다.');
      }
    },
    confirmBusinessNumber() {
      this.validBusinessNumber = true;
      this.showConfirmDialog = false;
    },
  },
};
</script>