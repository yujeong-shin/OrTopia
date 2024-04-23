<template>
    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title class="text-h5">비밀번호 찾기</v-card-title>
        <v-card-text>
          <v-form @submit.prevent="resetPassword">
            <v-text-field
              label="이메일"
              v-model="email"
              prepend-icon="mdi-email"
              type="email"
              :rules="emailRules"
              required
            ></v-text-field>
            <v-btn type="submit" color="primary" :disabled="!isValid" block>비밀번호 재설정 링크 보내기</v-btn>
            <v-btn color="red" text @click="closeDialog" block>닫기</v-btn> <!-- 닫기 버튼 추가하고 블록 레벨로 설정 -->
          </v-form>
        </v-card-text>
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
        email: '',
        emailRules: [
          v => !!v || '이메일은 필수입니다',
          v => /.+@.+\..+/.test(v) || '유효한 이메일 주소를 입력해주세요.'
        ],
      };
    },
    computed: {
      dialog: {
        get() { return this.value; },
        set(value) { this.$emit('update:dialog', value); }
      },
      isValid() {
        return this.emailRules.every(rule => rule(this.email) === true);
      }
    },
    methods: {
      async resetPassword() {
        try {
          const formData = new FormData();
          formData.append('email', this.email);
          const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/member/reset-password`, formData, {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          });
          alert(response.data);
          this.dialog = false;  // 성공 시 다이얼로그 닫기
        } catch (error) {
          console.error('Error sending reset link:', error.response || error);  
          alert(error.response?.data?.error_message || 'Failed to send reset link');
        }
      },
      closeDialog() {
        this.dialog = false;  // 다이얼로그 닫기 함수
      }
    }
  };
  </script>
  