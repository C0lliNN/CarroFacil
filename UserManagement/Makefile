PHONY: build-image


build-image:
	docker build -t cf-user-management .

deploy-image:
	docker tag cf-user-management:latest c0lllinn/cf-user-management:latest
	docker push c0lllinn/cf-user-management:latest