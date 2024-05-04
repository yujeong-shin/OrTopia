<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-card>
          <v-card-title class="headline grey lighten-2 text-center">
            회원가입
          </v-card-title>
          <v-card-text>
            <v-form>
              <v-text-field
                label="이메일"
                v-model="signupForm.email"
                prepend-icon="mdi-email"
                type="email"
              ></v-text-field>

              <v-text-field
                label="이름"
                v-model="signupForm.name"
                prepend-icon="mdi-account"
              ></v-text-field>

              <v-text-field
                label="비밀번호"
                v-model="signupForm.password"
                prepend-icon="mdi-lock"
                type="password"
              ></v-text-field>

              <v-text-field
                label="나이"
                v-model="signupForm.age"
                prepend-icon="mdi-calendar"
                type="number"
              ></v-text-field>

              <v-select
                label="성별"
                :items="['MALE', 'FEMALE']"
                v-model="signupForm.gender"
                prepend-icon="mdi-gender-male-female"
              ></v-select>

              <v-text-field
                label="전화번호"
                v-model="signupForm.phoneNumber"
                prepend-icon="mdi-phone"
                @blur="getPhoneMask(signupForm.phoneNumber)" 
                @input="limitPhoneNumberLength"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="success" @click="signup">회원가입</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router'; // Vue Router를 사용하기 위해 useRouter 가져오기

// 회원가입 양식 데이터
const signupForm = reactive({
  email: '',
  name: '',
  password: '',
  age: null,
  gender: '',
  phoneNumber: '',
});

const router = useRouter(); // useRouter 훅을 사용하여 router 인스턴스 가져오기

// 회원가입 함수
async function signup() {
  console.log('회원가입 시도:', signupForm);

  // 입력 검증 로직 (예시)
  if (!signupForm.email || !signupForm.password) {
    alert('이메일과 비밀번호를 입력해주세요.');
    return;
  }

  try {
    // axios를 사용하여 회원가입 요청 전송
    const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member/create`, {
      email: signupForm.email,
      name: signupForm.name,
      password: signupForm.password,
      age: signupForm.age,
      gender: signupForm.gender,
      phoneNumber: signupForm.phoneNumber.replace(/[^0-9]/g, ''),
    });

    // 성공 응답 처리
    console.log('회원가입 성공:', response.data);
    alert('회원가입에 성공했습니다. 로그인 페이지로 이동합니다.');

    // 성공 후 로그인 페이지로 리디렉션
    router.push('/login'); // '/login'은 로그인 페이지의 경로입니다. 프로젝트의 실제 경로에 맞게 수정해야 합니다.
  } catch (error) {
    // 오류 응답 처리
    console.error('회원가입 오류:', error.response);
    alert(`회원가입 오류: ${error.response.data.message}`);
  }
}
const limitPhoneNumberLength = () => {
  // 전화번호를 입력할 때마다 호출되며, 11자리로 제한합니다.
  if (signupForm.phoneNumber.length > 11) {
    alert("010xxxxxxxx 형식의 번호를 입력해 주세요.");
    signupForm.phoneNumber = signupForm.phoneNumber.slice(0, 11);
  }
};
const getPhoneMask = (val) => {
  if (!val) return; // 만약 전화번호가 없다면 함수를 종료합니다.
  let res = getMask(val);
  signupForm.phoneNumber = res;
  console.log(signupForm.phoneNumber);
  // 서버 전송 값에는 '-'를 제외하고 숫자만 저장합니다.
};
const getMask = ( phoneNumber ) => {
  if(!phoneNumber) return phoneNumber
  phoneNumber = phoneNumber.replace(/[^0-9]/g, '')
  let res = ''
  if(phoneNumber.length < 3) {
      res = phoneNumber
  }
  else {
      if(phoneNumber.substr(0, 2) =='02') {
  
          if(phoneNumber.length <= 5) {//02-123-5678
              res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 3)
          }
          else if(phoneNumber.length > 5 && phoneNumber.length <= 9) {//02-123-5678
              res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 3) + '-' + phoneNumber.substr(5)
          }
          else if(phoneNumber.length > 9) {//02-1234-5678
              res = phoneNumber.substr(0, 2) + '-' + phoneNumber.substr(2, 4) + '-' + phoneNumber.substr(6)
          }
  
      } else {
          if(phoneNumber.length < 8) {
              res = phoneNumber
          }
          else if(phoneNumber.length == 8)
          {
              res = phoneNumber.substr(0, 4) + '-' + phoneNumber.substr(4)
          }
          else if(phoneNumber.length == 9)
          {
              res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 3) + '-' + phoneNumber.substr(6)
          }
          else if(phoneNumber.length == 10)
          {
              res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 3) + '-' + phoneNumber.substr(6)
          }
          else if(phoneNumber.length > 10) { //010-1234-5678
              res = phoneNumber.substr(0, 3) + '-' + phoneNumber.substr(3, 4) + '-' + phoneNumber.substr(7)
          }
      }
      return res
  }
}
</script>

<style>
  .v-card-title {
    font-size: 28px; 
  }
</style>
