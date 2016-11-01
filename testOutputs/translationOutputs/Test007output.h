#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test007 {


	struct __A;
	struct __A_VT;
	struct __B;
	struct __B_VT;

	typedef __A* A;
	typedef __B* B;

	struct __A
	{

	String a;

	__A_VT* __vptr;

	__A();

	static Class __class();

	static int32_t hashCode(A);

	static __A_VT __vtable;


	};

	struct __A_VT
	{

	Class __isa;
	int32_t (*hashCode)(A);
	Class (*getClass)(A);
	bool (*equals)(A,Object);

	__A_VT()
	: __isa(__A::__class()),
		hashCode(&__A::hashCode),
		getClass((Class(*)(A)) &__Object::getClass),
		equals((bool(*)(A,Object)) &__Object::equals)
		{}

	};

	struct __B
	{

	String b;

	__B_VT* __vptr;

	__B();

	__A parent;

	static Class __class();

	static int32_t hashCode(B);

	static __B_VT __vtable;


	};

	struct __B_VT
	{

	Class __isa;
	int32_t (*hashCode)(B);
	Class (*getClass)(B);
	bool (*equals)(B,Object);

	__B_VT()
	: __isa(__B::__class()),
		hashCode(&__B::hashCode),
		getClass((Class(*)(B)) &__Object::getClass),
		equals((bool(*)(B,Object)) &__Object::equals)
		{}

	};



	}
}

