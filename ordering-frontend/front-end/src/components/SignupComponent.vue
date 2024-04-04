<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-card>
          <v-card-title class="text-h5">회원가입</v-card-title>
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
                type="tel"
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

const signupForm = reactive({
  email: '',
  name: '',
  password: '',
  age: null,
  gender: '',
  phoneNumber: '',
});

function signup() {
  console.log('회원가입 시도:', signupForm);
  // 여기에 회원가입 로직을 추가하세요.
}
</script>
