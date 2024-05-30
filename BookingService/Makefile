PHONY: build-image


build-image:
	docker build -t cf-booking-service .

deploy-image:
	docker tag cf-booking-service:latest c0lllinn/cf-booking-service:latest
	docker push c0lllinn/cf-booking-service:latest