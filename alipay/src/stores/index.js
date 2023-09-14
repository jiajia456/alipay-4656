import { createStore } from 'vuex';

const store = createStore({
    state: {
        isAuthenticated: false, // 默认未登录
        // 其他状态...
    },
    mutations: {
        setIsAuthenticated(state, value) {
            state.isAuthenticated = value;
        },
        // 其他 mutations...
    },
    actions: {
        // 其他 actions...
    },
    modules: {
        // 其他模块...
    },
});

export default store;