# AIDL

> AIDL测试用例，完整介绍请点击[这里][1]

 AIDL (Android Interface Definition Language) 是一种IDL 语言，用于生成可以在Android设备上两个进程之间进行进程间通信(interprocess communication, IPC)的代码。如果在一个进程中（例如Activity）要调用另一个进程中（例如Service）对象的操作，就可以使用AIDL生成可序列化的参数。

AIDL的作用

由于每个应用程序都运行在自己的进程空间，并且可以从应用程序UI运行另一个服务进程，而且经常会在不同的进程间传递对象。在Android平台，一个进程通常不能访问另一个进程的内存空间，所以要想对话，需要将对象分解成操作系统可以理解的基本单元，并且有序的通过进程边界。通过代码来实现这个数据传输过程是冗长乏味的，所以Android提供了AIDL工具来处理这项工作。

选择AIDL的使用场合

官方文档特别提醒我们何时使用AIDL是必要的：只有你允许客户端从不同的应用程序为了进程间的通信而去访问你的service，以及想在你的service处理多线程。

如果不需要进行不同应用程序间的并发通信(IPC)，you should create your interface by implementing a Binder；或者你想进行IPC，但不需要处理多线程的，则implement your interface using a Messenger。无论如何，在使用AIDL前，必须要理解如何绑定service——bindService，可以查看Android四大组件之Service。

在设计AIDL接口前，要提醒的是，调用AIDL接口是直接的方法调用的，不是我们所想象的调用是发生在线程里。而调用(call)来自local进程或者remote进程。


  [1]: http://www.27house.cn/android-ipc%E4%B9%8Baidl/