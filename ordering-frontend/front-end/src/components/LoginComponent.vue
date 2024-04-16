<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card>
          <v-card-title class="text-h5">로그인</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="doLogin">
              <v-text-field
                label="이메일"
                v-model="email"
                prepend-icon="mdi-email"
                type="email"
                :rules="emailRules"
                required
              ></v-text-field>
              <v-text-field
                label="비밀번호"
                v-model="password"
                prepend-icon="mdi-lock"
                type="password"
                :rules="passwordRules"
                required
              ></v-text-field>
              <v-row>
                <v-col cols="6">
                  <v-btn color="green" @click="showFindIdModal" block>아이디 찾기</v-btn>
                </v-col>
                <v-col cols="6">
                  <v-btn type="submit" color="primary" :disabled="!valid" block>로그인</v-btn>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <!-- FindIdModal 컴포넌트에 v-model로 findIdDialog 바인딩하고, @update:dialog 이벤트를 통해 상태 업데이트 -->
    <FindIdModal
      v-model="findIdDialog"
      @update:dialog="findIdDialog = $event"
    ></FindIdModal>
  </v-container>
</template>


<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import FindIdModal from '@/components/FindIdModal.vue';

export default {
  components: {
    FindIdModal
  },
  data() {
    return {
      email: '',
      password: '',
      findIdDialog: false,
      emailRules: [
        v => !!v || '이메일은 필수입니다',
        v => /.+@.+\..+/.test(v) || '유효한 이메일 주소를 입력해주세요.',
      ],
      passwordRules: [
        v => !!v || '비밀번호는 필수입니다',
      ],
    };
  },
  computed: {
    valid() {
      return this.email && this.password;
    }
  },
  methods: {
    async doLogin() {
      try {
        const loginData = { email: this.email, password: this.password };
        const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/doLogin`, loginData);
        const accessToken = response.data.result.accessToken;
        const refreshToken = response.data.result.refreshToken; // refreshToken 추가

        console.log(accessToken);

        if (accessToken && refreshToken) {
          const decoded = jwtDecode(accessToken);
          const role = decoded.role;
          const email = decoded.sub;
          localStorage.setItem('email', email);
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
    // FindIdModal을 열기 위한 메소드
    showFindIdModal() {
      this.findIdDialog = true;
    },
  }
};
</script>
