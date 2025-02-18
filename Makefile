clean:
	make -C app clean

build:
	make -C app clean build

install:
	make -C app clean install

run-dist:
	make -C app run-dist

run:
	make -C app run

test:
	make -C app test

report:
	make -C app report

lint:
	make -C app lint

update-deps:
	make -C app update-deps