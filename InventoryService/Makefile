PHONY: build-image


build-image:
	docker build -t cf-inventory-management .

deploy-image:
	docker tag cf-inventory-management:latest c0lllinn/cf-inventory-management:latest
	docker push c0lllinn/cf-inventory-management:latest