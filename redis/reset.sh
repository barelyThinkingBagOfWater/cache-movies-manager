docker stop redis &&
	docker run --rm -d --name redis --network isolatedNetwork redis &&
docker logs --follow redis
