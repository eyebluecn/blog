### fork文件夹中修改内容

1. 为了加快资源加载速度， editormd中所有 `//cdnjs.cloudflare.com/ajax/libs/KaTeX/0.3.0/katex.min` 替换成 `editormd.defaults.path + "katex/katex.min"` ；如果是在`.min.js`中就直接替换成`/bystatic/fork/editormd/lib/katex/katex.min`

