import { getToken, setToken, removeToken } from '@/common/utils/auth'
import { isRequestSuccess, getResponseContent } from '@/common/utils/common'
import { Message } from 'element-ui'

const user = {
  state: {
    curUserId: '',
    user: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    avatar: '',
    introduction: '',
    roles: [],
    pris: {},
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_CUR_USERID: (state, id) => {
      state.curUserId = id
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PRIS: (state, pris) => {
      state.pris = pris
    }

  },

  actions: {
  }

}

export default user
