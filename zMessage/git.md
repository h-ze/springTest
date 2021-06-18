git checkout heze 切换到本地分支
git add .
git commit -m "操作"
git pull origin heze 从heze分支上往下拉代码
git push origin heze 上传到heze分支上
git log 查看日志记录
git branch 查看分支
git checkout -b 分支  创建分支并切换到该分支（）

git checkout -b local origin/WUYINGYUN 创建分支并切换到该分支并拉取远端分支的代码


git merge 分支 merge某个分支到当前分支

有时候，进行了错误的提交，但是还没有push到远程分支，想要撤销本次提交，可以使用git reset –-soft/hard命令
git reset 版本号（至少6位）
git reset --hard   # 回到最新的一次提交(彻底回到某个版本)
git reset --soft 回退到某个版本，只回退了commit的信息，不会恢复到index file一级。如果还要提交，直接commit即可
git status 查看状态
git rm -r --cached 文件名   清除缓存的文件，一般用于清除已经commit的未被忽略的文件 删除远程上的文件

<<<<<<和================是本地的
================和>>>>>>>>> 是其他提交引入的内容

该代码段行开始<<<<<<和================这里之间：

<<<<<< HEAD 
some code snippet here

================

就是你本地已经拥有的东西 - 你可以说，因为HEAD指向你当前的分支或提交。开始================和>>>>>>>>> 的代码片段：

================

some code snippet here

adsf23423423423423 >>>>>>>>>

是其他提交所引入的内容，在本例中为adsf23423423423423。这是合并到HEAD中的提交的对象名称（或“散列”，“SHA1sum”等）。git中的所有对象，无论它们是提交（版本），blob（文件），tree（目录）还是标签都有这样的对象名称，它们根据其内容唯一标识它们。

现在可以决定是否要保留头文件的代码段或由adsf23423423423423提交引入的代码段。

如果从从未触摸过的代码中得到此合并冲突，请保留adsf23423423423423提交引入的代码。



git如果Unstaged changes after reset的问题
可以使用 
git stash
git stash drop