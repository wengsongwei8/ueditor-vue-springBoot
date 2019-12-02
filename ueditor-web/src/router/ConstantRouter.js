/**
 *  公用的路由定义
 */
import Layout from '../views/layout/Layout'
const _import = require('./_import_' + process.env.NODE_ENV)

export default  [
  { path: '/', component: _import('udeitorDemo/index'), hidden: true },
  { path: '/authredirect', component: _import('udeitorDemo/authredirect'), hidden: true },
  { path: '/404', component: _import('common/errorPage/404'), hidden: true },
  { path: '/401', component: _import('common/errorPage/401'), hidden: true }

];



