import {createStore} from 'vuex';
// 2개의 상태를 업데이트, 트래킹
// 1. 구매예정 아이템 목록
// 2. totalQuantity 전체 수량


// initState, updateLocalStorage와 같은 함수는 스터오 정의 바깥에 위치
// 책임과 권한을 분리하는 개념적인 의도도 있지만, 다른 스토어나 다른 상황에서 재사용성을 높이기 위한 아키텍처

// 초기화, 리턴
function initState(){
    return{
        // 담는 부분
        cartItem: JSON.parse(localStorage.getItem('cartItems')) || [],
        totalQuantity: localStorage.getItem('totalQuantity') || 0,
        buyerId: null
    }
}
function updateLocalStorage(cartItems,totalQuantity){
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
    localStorage.setItem('totalQuantity',totalQuantity);
}


export default createStore({
    // state : 상태를 의미하는 객체로서 initState를 통해 상태초기화를 하고 있다
    state: initState,

    // mutations는 상태를 변경하는 함수들의 집합
    // vuex에서 커밋이라는 용어는 상태변경을 위해 mutation을 호출하는 과정을 의미
    mutations:{
        // addToCart함수는 외부컴포넌트 또는 액션에서 호출
        addToCart(state,item){
            const existItem = state.cartItem.find(i => i.id == item.id);
            if(existItem){
                console.log(state.cartItem);
                existItem.count += item.count;
            }else{
                state.cartItem.push(item);
                state.totalQuantity = parseInt(state.totalQuantity) + 1;
            }
            // totalCount
            updateLocalStorage(state.cartItem, state.totalQuantity);
        },
        clearCart(state){
            state.cartItem = [];
            state.totalQuantity =0;
            updateLocalStorage(state.cartItem,state.totalQuantity);
            //localStorage.removeItem("cartItems");
            //localStorage.removeItem("totalQuantity");
            //window.location.reload();
        },
        setBuyerId(state, buyerId) {
            state.buyerId = buyerId;  // buyerId 상태 업데이트
        }
    },
    // actions를 통해 여러 mutationa을 커밋하거나, 비동기 작업을 진행, 일반적으로 component에서 actions의 method를 호출하고
    // actions에서 mutation 함수를 호출한다.  
    actions:{
        // context는 매개변수가 주입이 되고 context 매개변수 안에 state, commit 등이 존재
        addToCart(context, item){
            context.commit('addToCart',item);
        },
        clearCart(context){
            context.commit('clearCart');
        },
        setBuyerId(context, buyerId) {
            context.commit('setBuyerId', buyerId);
        }
    },
    // getter : 상태를 반환하는 함수들의 집합
    getters:{
        getCartItems : state=> state.cartItem,
        getTotalQuantity : state=> state.totalQuantity,
        getBuyerId: state => state.buyerId
    },
});