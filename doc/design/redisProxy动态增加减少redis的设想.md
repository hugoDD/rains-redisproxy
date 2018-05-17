redisProxy中间动态增加减少redis的设想
==========================================

redisProxy作为redis代理的中间件，目的是为了简单、轻量级维护redis动态扩容。

#概念

* **groupNode**
    * groupNode包括多个redis的master与从节点slave,redisProxy把groupNode看作一整体，每个groupNdoe设置128个slot.也就是说每个groupNode最多有128个master节点。
    * master可以有从多个slave,建议master->slave->slave模式。master宕机时，由监控程序把slave切换成master,原master再次启动时变成slave
    * redisProxy对groupNode所有节点做权限管理，实现多租户架构。每个租户可以使用一个或者多个groupNode.一个groupNode只可以被一个租户使用。
    * 当增加redis节点时，首先考虑增加groupNode.再对groupNode授权。

* **viewKeys**
    * viewKeys所有redis节点的集合。采用主从同步协议，备份所有的keys到(es或者redis集群),viewKeys为可选部署.
    * 分离keys * 命令，redisProxy判断如果是keys * 命令时，直接到viewKeys查询，返回分页
    * 如果使用es存储keys，可以实现对keys的分词搜索功能
    * 当groupNode内增加一个redis master时，一个获取key如果hash到新增redis master的那个slot获取不到之前已经缓存的值，就到viewKeys查询到当前key真正存储的redis节点
    * 当groupNode内增加一个redis master时，可以通过viewKeys获取新增redis master当前slot的备份缓存 






* **将从redis设成主redis**
    [root@localhost redis-2.8.3]# src/redis-cli -p 6380 slaveof NO ONE
    OK