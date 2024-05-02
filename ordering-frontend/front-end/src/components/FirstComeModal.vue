<template>
    <v-dialog v-model="internalShow" max-width="600px">
        <v-card>
            <v-card-title class="headline">{{ coupon.couponDetailResponseDto.name || '선착순 쿠폰' }}</v-card-title>
            <v-card-text v-if="couponLoaded">
                <p>이 쿠폰은 제한된 수량 {{ coupon.couponDetailResponseDto.firstCome }}명만큼만 발행됩니다. 서둘러서 쿠폰을 받으세요!</p>
                <p>할인율: {{ coupon.couponDetailResponseDto.rateDiscount }}%</p>
                <p>할인 금액: {{ coupon.couponDetailResponseDto.fixDiscount }}원</p>
                <p>유효 기간: {{ coupon.couponDetailResponseDto.startDate }} ~ {{ coupon.couponDetailResponseDto.endDate }}</p>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green" text @click="claimCoupon" :disabled="!canClaimCoupon">쿠폰 받기</v-btn>
                <v-btn color="red darken-1" text @click="close">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';

export default {
    props: {
        modelValue: Boolean,
        itemId: Number
    },
    data() {
        return {
            coupon: {
                couponDetailResponseDto: {}
            },
            couponLoaded: false,
            alreadyClaimed: false
        };
    },
    computed: {
        internalShow: {
            get() {
                return this.modelValue;
            },
            set(value) {
                this.$emit('update:modelValue', value);
            }
        },
        canClaimCoupon() {
            return this.couponLoaded && !this.alreadyClaimed;
        }
    },
    methods: {
        close() {
            this.$emit('update:modelValue', false);
        },
        fetchCoupon() {
            const token = localStorage.getItem('accessToken');
            const refreshToken = localStorage.getItem("refreshToken");

            console.log('Fetching coupon for itemId:', this.itemId);

            axios.get(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/coupon/${this.itemId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "X-Refresh-Token": `${refreshToken}`,
                }
            })
            .then(response => {
                console.log('Coupon data received:', response.data);
                if (response.data.result.length > 0 && response.data.result[0].couponDetailResponseDto) {
                    this.coupon = response.data.result[0];
                    this.couponLoaded = true;
                } else {
                    this.couponLoaded = false;
                    console.error("No coupon details available.");
                }
            })
            .catch(error => {
                console.error("Error fetching coupon:", error.response || error);
                alert('쿠폰 정보를 불러오는데 실패했습니다.');
                this.couponLoaded = false;
            });
        },
        claimCoupon() {
            const token = localStorage.getItem('accessToken');
            const email = localStorage.getItem('userEmail');

            axios.post(`${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/coupons`, {
                couponId: this.coupon.id
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'myEmail': email
                }
            })
            .then(response => {
                if (response.data.status === "ALREADY_CLAIMED") {
                    this.alreadyClaimed = true;
                    alert('이미 이 쿠폰을 받으셨습니다.');
                } else {
                    alert('쿠폰이 성공적으로 추가되었습니다!');
                    this.alreadyClaimed = true;
                }
            })
            .catch(error => {
                console.error('Error claiming coupon:', error);
                alert('쿠폰을 받는데 실패했습니다.');
            });
        }
    },
    watch: {
        modelValue(newValue) {
            if (newValue && this.itemId) {
                this.fetchCoupon();
            }
        }
    }
};
</script>