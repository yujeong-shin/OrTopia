<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card>
          <v-card-title class="text-h5">로그인</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="doLogin"> <!-- form submit 이벤트 처리 추가 -->
              <v-text-field
                label="이메일"
                v-model="email"
                prepend-icon="mdi-email"
                type="email"
              ></v-text-field>
              <v-text-field
                label="비밀번호"
                v-model="password"
                prepend-icon="mdi-lock"
                type="password"
              ></v-text-field>
              <v-btn type="submit" color="primary">로그인</v-btn> <!-- 버튼 클릭 대신 form submit 사용 -->
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // jwt-decode 라이브러리 import 확인

export default {
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    async doLogin() {
  try {
    const loginData = { email: this.email, password: this.password };
    const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/doLogin`, loginData);
    const accessToken = response.data.accessToken;
    const refreshToken = response.data.refreshToken; // refreshToken 추가

    console.log(accessToken);

    if (accessToken && refreshToken) {
      const decoded = jwtDecode(accessToken);
      const role = decoded.role;

      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('role', role);
      localStorage.setItem('refreshToken', refreshToken); // refreshToken을 localStorage에 저장
      window.location.href =  "/";
    } else {
      console.log('200 OK, but no token');
      alert('Login failed');
    }
  } catch (error) {
    const errorMessage = error.response?.data?.error_message || 'Login failed';
    console.error(errorMessage);
    alert(errorMessage);
  }
},
  },
};
</script>
