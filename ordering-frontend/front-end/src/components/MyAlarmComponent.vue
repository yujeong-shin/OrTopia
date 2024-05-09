<template>
  <v-container>
    <v-col cols="12">
      <v-card-text>
        <v-card-title class="headline grey lighten-2 text-center" style="font-size: 40px;">
          알람
        </v-card-title>
        <div class="reminder-message">* 한번 확인을 한 알람을 사라집니다. *</div>
        <br>
        <!-- 조건부 렌더링을 사용하여 alarmMessages 배열이 비어 있으면 알림이 없음을 알리는 메시지를 표시합니다. -->
        <div v-if="alarmMessages.length === 0" class="no-alarm-message">모든 알람을 확인 했어요!</div>
        <div v-else>
          <!-- alarmMessages 배열이 비어 있지 않은 경우 알람 메시지를 표시합니다. -->
          <v-card v-for="(message, index) in alarmMessages" :key="index" class="alarm-card" @click="handleCardClick(message)">
      <div class="alarm-message">
        <span class="alarm-date">{{ formatDate(message) }}</span> 
        <span class="alarm-info">{{ formatMessage(message) }}</span>
      </div>
    </v-card>
        </div>
      </v-card-text>
    </v-col>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      alarmMessages: [],
    };
  },
  created() {
    this.loadAlarmMessages();
  },
  methods: {
    handleCardClick(message) {
      // 클릭된 카드의 메시지를 처리하는 함수
      let itemId = "none";
      const parts = message.split('_');
      if (parts.length === 3) {
        const itemIdarr = parts[2].split(':'); // itemId:51
        itemId = itemIdarr[1];
      }
      console.log(itemId);
      window.location.href = `/item/${itemId}`;
      // 필요한 로직을 여기에 추가하세요
    },
    loadAlarmMessages() {
      const storedArray = localStorage.getItem('alarmMessage');
      if (storedArray) {
        try {
          this.alarmMessages = JSON.parse(storedArray);
        } catch (error) {
          console.error('Error parsing JSON:', error);
          this.alarmMessages = [];
        }
      } else {
        this.alarmMessages = [];
      }
      localStorage.setItem('alarmMessage',[]);
      localStorage.setItem('alarm', 0);
    },
    formatDate(dateString) {
      // 날짜를 적절한 형식으로 변환하여 반환하는 함수
      // 예: "2024.05.03_쿠팡이 님이 새로운 아이템 아이템 등록 테스트을 등록했어요!"
      const parts = dateString.split('_');
      if (parts.length === 3) {
        return parts[0]; // "2024.05.03"
      }
      return dateString; // 변환할 수 없는 경우 그대로 반환
    },
    formatMessage(dateString) {
      const parts = dateString.split('_');
      if (parts.length === 3) {
        return parts[1]; 
      }
      return dateString; 
    },
  },
};
</script>

<style scoped>
/* 추가적인 스타일링을 원하는 경우 여기에 추가하세요 */
.no-alarm-message {
  font-size: 24px; /* 텍스트의 크기를 24px로 지정합니다. */
  text-align: center;
}
.alarm-message {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  margin-top: 12px;
}

.alarm-card {
  margin: auto;
  width: 70%; /* 원하는 너비 설정 */
}
.alarm-date {
  font-weight: bold;
  font-size: 18px;
}

.alarm-info {
  flex-grow: 1;
  margin-left: 8px;
  font-size: 18px; 
}
.reminder-message {
  text-align: right;; /* 텍스트를 왼쪽으로 정렬합니다. */
  margin-top: 16px; /* 위쪽 여백을 추가합니다. */
  color: gray;
}
</style>