import Vue from 'vue'
import VueRouter from 'vue-router'

// 路由懒加载
const Login = () => import(/* webpackChunkName: "login" */ '../views/Login.vue')
const Layout = () => import(/* webpackChunkName: "layout" */ '../components/layout/index')
const Home = () => import(/* webpackChunkName: "layout" */ '../views/Home')

Vue.use(VueRouter)

export const constantRouterMap = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/layout',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: '/home',
        component: Home,
        meta: { title: '首页', icon: 'index', affix: true, noCache: true }
      }
    ]
  }
]

const index = new VueRouter({
  routes: constantRouterMap
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

// 挂载路由导航守卫
index.beforeEach((to, from, next) => {
  // to:将要访问的路径
  // from:代表从哪个路径跳转而来
  // next:是一个函数，表示放行（next():放行， next('/login'):强制跳转）
  if (to.path === '/login') return next()
  // 获取token
  const token = window.sessionStorage.getItem('token')
  console.log(token)
  if (!token) return next('/login')
  next()
})

export default index
