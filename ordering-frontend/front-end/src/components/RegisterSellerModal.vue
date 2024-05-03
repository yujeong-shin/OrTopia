<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1000px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        판매자등록
      </v-card-title>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6">
          <v-card>
            <v-card-text>
              <v-form
                ref="form"
                @submit.prevent="submitSellerForm"
                v-model="valid"
              >
                <v-text-field
                  label="사업자 번호"
                  v-model="sellerForm.businessNumber"
                  :rules="businessNumberRules"
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
                  :items="[
                    '가구',
                    '도서',
                    '가전',
                    '생활',
                    '건강',
                    '스포츠',
                    '식품',
                    '육아',
                    '의류',
                    '잡화',
                    '화장품',
                  ]"
                  :rules="businessTypeRules"
                  required
                ></v-select>

                <v-btn type="submit" color="success" :disabled="!valid || !validBusinessNumber"
                  >등록하기</v-btn
                >
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
import axios from "axios";
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
        businessNumber: "",
        companyName: "",
        businessType: "",
      },
      validBusinessNumber: false,
      businessNumberRules: [
        (v) => !!v || "사업자 번호는 필수입니다.",
        (v) => /^\d{10}$/.test(v) || "사업자 번호는 10자리 숫자여야 합니다.",
      ],
      companyNameRules: [(v) => !!v || "회사명은 필수입니다."],
      businessTypeRules: [(v) => !!v || "사업 유형은 필수입니다."],
    };
  },
  watch: {
    'sellerForm.businessNumber': function (newVal, oldVal) {
      if (newVal !== oldVal) {
        this.validBusinessNumber = false;
      }
    }
  },
  methods: {
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
    async submitSellerForm() {
      if (!this.validBusinessNumber) {
        alert('사업자 번호를 검증하세요.');
        return;
      }
      console.log("판매자 등록 시도:", this.sellerForm);
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        console.log("판매자 등록 요청을 보냅니다...");
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/seller/${email}/create`,
          {
            businessNumber: this.sellerForm.businessNumber,
            companyName: this.sellerForm.companyName,
            businessType: this.sellerForm.businessType,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
              "Content-Type": "application/json",
              myEmail: `${email}`,
            },
          }
        );
        console.log("Seller registered successfully", response.data);
        localStorage.setItem("role", "SELLER");
        alert("판매자 등록 성공!");
        window.location.href = "/";
      } catch (error) {
        console.error("판매자 등록 오류:", error);
        alert(`등록 실패: ${error.response.data.message}`);
      }
    },
    async checkBusinessNumber() {
      const data = { "b_no": [this.sellerForm.businessNumber] };
      try {
        const response = await axios.post(
          `https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=crGL2N15kbZz5KnajRhQC4Pur3KT5XFRX%2BBLM6DB4XNqet1Kp54PUOYMS2UrNAfdMkvD%2FXKV7FOTrcoRhvdtQQ%3D%3D`,
          JSON.stringify(data),
          {
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json'
            }
          }
        );
        console.log("API 응답:", response);
        if (response.data.data[0].b_stt_cd === '01') {
          this.validBusinessNumber = true;
          alert('사업자 번호가 유효합니다.');
        } else {
          this.validBusinessNumber = false;
          alert('사업자 번호가 유효하지 않습니다. 관리자에게 문의하세요.');
        }
      } catch (error) {
        console.error('API 호출 오류:', error);
        alert('사업자 번호 검증 중 오류가 발생했습니다: ' + (error.response ? error.response.data.message : error.message));
      }
    },
  },
};
</script>