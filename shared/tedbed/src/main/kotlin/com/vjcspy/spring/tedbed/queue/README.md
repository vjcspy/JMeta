# Spring RabbitMQ

## Expectations

Khi làm việc với RabbitMQ cần đảm bảo được các yếu tố sau

* [ ] Context: Chúng ta có 3 contexts đó là: request, job, queue. Trong trường hợp này mình cần implement context cho queue. Lưu ý, với queue phải đảm bảo được
  * [ ] Khi start thì context phải refresh `x-correlationid`, gặp lỗi hoặc kết thúc thì sẽ phải clear 
  * [ ] Đảm bảo mỗi một consumer một lúc chỉ chạy 1 thread và xử lý 1 message => Cực kì quan trọng vì nếu không sẽ bị sai `context` 
