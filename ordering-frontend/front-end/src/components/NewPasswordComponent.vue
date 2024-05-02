<template>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6">
          <v-card class="pa-5">
            <v-card-title class="text-h5 text-center">비밀번호 재설정</v-card-title>
            <v-card-text>
              <v-form ref="form" @submit.prevent="resetPassword">
                <v-text-field
                  v-model="password"
                  :rules="passwordRules"
                  label="새 비밀번호"
                  type="password"
                  prepend-icon="mdi-lock"
                  required
                ></v-text-field>
                <v-text-field
                  v-model="confirmPassword"
                  :rules="confirmPasswordRules"
                  label="비밀번호 확인"
                  type="password"
                  prepend-icon="mdi-lock-check"
                  required
                ></v-text-field>
                <v-btn color="success" block type="submit">비밀번호 변경</v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
<script>
import axios from 'axios';

export default {
  
  data() {
    return {
      password: '',
      confirmPassword: '',
      passwordRules: [
        v => !!v || '비밀번호는 필수입니다',
        v => v.length >= 8 || '비밀번호는 8자 이상이어야 합니다',
      ],
      confirmPasswordRules: [
        v => !!v || '비밀번호 확인은 필수입니다',
        v => v === this.password || '비밀번호가 일치하지 않습니다',
      ],
    };
  },
  methods: {
    async resetPassword() {
      if (this.$refs.form.validate()) {
        try {
          const formData = new FormData();
           const token = this.$route.query.token;  // Vue Router를 사용하여 토큰을 가져옵니다.
          formData.append('token', token);
          formData.append('password', this.password);
          await axios.post(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member/change-password`, formData, {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          });
          alert('비밀번호가 성공적으로 변경되었습니다.');
          this.$router.push('/login');  // 로그인 페이지로 이동
        } catch (error) {
          console.error('Error resetting password:', error.response || error);
          alert('비밀번호 재설정에 실패했습니다. 다시 시도해주세요.');
        }
      }
    }
  }
};
</script>
