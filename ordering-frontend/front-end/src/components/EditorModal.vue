<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-card-title class="justify-space-between">
        <span>글쓰기</span>
      </v-card-title>
      <v-card-text>
        <!-- 공지 이름 입력 -->
        <v-text-field
          v-model="notice.name"
          label="공지 이름"
          outlined
          dense
        ></v-text-field>
        
        <!-- 시작 날짜 선택 -->
        <v-menu
          v-model="startMenu"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="notice.startDate"
              label="시작 날짜"
              prepend-icon="mdi-calendar"
              v-bind="attrs"
              v-on="on"
              outlined
              dense
            ></v-text-field>
          </template>
          <v-date-picker v-model="notice.startDate" @input="startMenu = false"></v-date-picker>
        </v-menu>

        <!-- 종료 날짜 선택 -->
        <v-menu
          v-model="endMenu"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="notice.endDate"
              label="종료 날짜"
              prepend-icon="mdi-calendar"
              v-bind="attrs"
              v-on="on"
              outlined
              dense
            ></v-text-field>
          </template>
          <v-date-picker v-model="notice.endDate" @input="endMenu = false"></v-date-picker>
        </v-menu>

        <!-- 텍스트 에디터 -->
        <text-editor v-model="contents" />
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="saveContent">저장하기</v-btn>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
  
<script>
import TextEditor from "@/components/TextEditor.vue";
import axios from "axios";

export default {
  components: {
    TextEditor,
  },
  props: {
    value: Boolean,
  },
  emits: ['update:value'],
  data() {
    return {
      dialog: this.value,
      notice: {
        name: '',
        startDate: '',
        endDate: '',
        contents: '',
      },
      selectedFile: null,
      contents: '', 
      startMenu: false,
      endMenu: false,
    };
  },
  watch: {
    value(val) {
      this.dialog = val;
    },
    dialog(val) {
      this.$emit('update:value', val);
    }
  },
  methods: {
    close() {
      window.location.reload();
    },
    saveContent() {
  const noticeData = JSON.stringify({
    name: this.notice.name,
    startDate: this.notice.startDate,
    endDate: this.notice.endDate,
    contents: this.contents
  });

  const config = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      "X-Refresh-Token": localStorage.getItem('refreshToken'),
    },
  };

  axios.post(`${process.env.VUE_APP_API_BASE_URL}/notice-service/create`, noticeData, config)
    .then(response => {
      console.log(response.data);
      window.location.reload();
    })
    .catch(error => {
      console.error("저장 중 에러 발생: ", error);
    });
},
  },
}
</script>